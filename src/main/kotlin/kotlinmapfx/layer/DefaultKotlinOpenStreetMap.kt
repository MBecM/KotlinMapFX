package kotlinmapfx.layer

import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.controller.DefaultMapController
import kotlinmapfx.layer.controller.MapController
import kotlinmapfx.layer.tile.DefaultTiledMap
import kotlinmapfx.layer.tile.MapOperations
import kotlinmapfx.layer.tile.TiledMap
import javafx.scene.Parent
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Region
import kotlinmapfx.layer.tile.LayeredMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Mateusz Becker
 */
abstract class DefaultKotlinOpenStreetMap(private val tiledMap: TiledMap = DefaultTiledMap(), val controller: MapController = DefaultMapController(tiledMap)) : Region(), KotlinOpenStreetMap, MapOperations by tiledMap, LayeredMap by tiledMap {

    private var coordinateConsumer: ((LatLon) -> Unit)? = null
    private var consumerButton: MouseButton? = null

    init {
        children.add(tiledMap.getView())
        children.add(TextField("54.5745,18.3908").apply {
            translateY = 100.0
            setOnAction {
                tiledMap.center(LatLon(this.text.substringBefore(",").toDouble(), this.text.substringAfter(",").toDouble()), 16)
            }
        })
        addEventHandler(MouseEvent.MOUSE_PRESSED, controller::mousePressed)
        addEventHandler(MouseEvent.MOUSE_DRAGGED, controller::mouseDragged)
        addEventHandler(ScrollEvent.SCROLL, controller::scroll)

        addEventHandler(MouseEvent.MOUSE_CLICKED, { ev ->
            coordinateConsumer?.let { consumer ->
                consumerButton?.let {
                    if (ev.button == it) {
                        consumer.invoke(tiledMap.getCoordinate(ev.x, ev.y))
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

    protected fun layer(order: Double = 0.0): ReadOnlyProperty<DefaultKotlinOpenStreetMap, Layer> = object : ReadOnlyProperty<DefaultKotlinOpenStreetMap, Layer> {
        var layer: MarkerLayer? = null

        override fun getValue(thisRef: DefaultKotlinOpenStreetMap, property: KProperty<*>): Layer {
            if (layer == null) {
                layer = MarkerLayer(tiledMap)
                addLayer(layer!!, order)
            }
            return layer as Layer
        }
    }
}