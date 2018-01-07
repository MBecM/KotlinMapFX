package kotlinmapfx

import kotlinmapfx.coord.LatLon
import kotlinmapfx.controller.DefaultMapController
import kotlinmapfx.controller.MapController
import kotlinmapfx.layer.DefaultTiledLayeredView
import kotlinmapfx.layer.MapOperations
import kotlinmapfx.layer.TiledLayeredView
import javafx.scene.Parent
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Region
import kotlinmapfx.layer.Layer
import kotlinmapfx.layer.ComponentLayer
import kotlinmapfx.layer.LayeredMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Mateusz Becker
 */
abstract class AbstractKotlinOpenStreetMap(private val tiledLayeredView: TiledLayeredView = DefaultTiledLayeredView(), val controller: MapController = DefaultMapController(tiledLayeredView)) : Region(), KotlinOpenStreetMap, MapOperations by tiledLayeredView, LayeredMap by tiledLayeredView {

    private var coordinateConsumer: ((LatLon) -> Unit)? = null
    private var consumerButton: MouseButton? = null

    init {
        children.add(tiledLayeredView.getView())
        children.add(TextField("54.5745,18.3908").apply {
            translateY = 100.0
            setOnAction {
                tiledLayeredView.center(LatLon(this.text.substringBefore(",").toDouble(), this.text.substringAfter(",").toDouble()), 16)
            }
        })
        addEventHandler(MouseEvent.MOUSE_PRESSED, controller::mousePressed)
        addEventHandler(MouseEvent.MOUSE_DRAGGED, controller::mouseDragged)
        addEventHandler(ScrollEvent.SCROLL, controller::scroll)

        addEventHandler(MouseEvent.MOUSE_CLICKED, { ev ->
            coordinateConsumer?.let { consumer ->
                consumerButton?.let {
                    if (ev.button == it) {
                        consumer.invoke(tiledLayeredView.getCoordinate(ev.x, ev.y))
                    }
                }
            }
        })

    }

    override fun setCoordinateConsumer(consumerButton: MouseButton, consumer: (LatLon) -> Unit) {
        this.coordinateConsumer = consumer
        this.consumerButton = consumerButton
    }

    override fun disableCoordinateConsumer() {
        this.coordinateConsumer = null
        this.consumerButton = null
    }

    override fun getView(): Parent {
        return this
    }

    protected fun layer(): ReadOnlyProperty<AbstractKotlinOpenStreetMap, Layer> = object : ReadOnlyProperty<AbstractKotlinOpenStreetMap, Layer> {
        var layer: ComponentLayer? = null

        override fun getValue(thisRef: AbstractKotlinOpenStreetMap, property: KProperty<*>): Layer {
            if (layer == null) {
                layer = ComponentLayer(tiledLayeredView)
                addLayer(layer!!)
            }
            return layer as Layer
        }
    }
}