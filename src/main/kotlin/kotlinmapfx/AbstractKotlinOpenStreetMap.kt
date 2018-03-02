package kotlinmapfx

import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle
import kotlinmapfx.controller.DefaultMapController
import kotlinmapfx.controller.MapController
import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.*
import kotlinmapfx.layer.tile.SimpleOSMTilesProvider
import kotlinmapfx.layer.tile.TilesProvider
import mu.KotlinLogging
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private val log = KotlinLogging.logger { }

/**
 * @author Mateusz Becker
 */
abstract class AbstractKotlinOpenStreetMap(tilesProvider: TilesProvider = SimpleOSMTilesProvider(), private val tiledLayeredView: TiledLayeredView = DefaultTiledLayeredView(tilesProvider), private val controller: MapController = DefaultMapController(tiledLayeredView)) : Region(), KotlinOpenStreetMap, MapOperations by tiledLayeredView, LayeredMap by tiledLayeredView {

    private var coordinateConsumer: ((LatLon) -> Unit)? = null
    private var consumerButton: MouseButton? = null
    private val mapClip = Rectangle(200.0, 200.0)

    init {
        clip = mapClip
        children.add(tiledLayeredView.getView())
        val overlay = StackPane().apply {
            children += Label(tilesProvider.attribution).apply {
                style = "-fx-background-color: rgba(0.3, 0.3, 0.3, 0.3);"
                StackPane.setAlignment(this, Pos.BOTTOM_RIGHT)
            }
        }
        overlay.prefWidthProperty().bind(mapClip.widthProperty())
        overlay.prefHeightProperty().bind(mapClip.heightProperty())
        children += overlay

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

        layoutBoundsProperty().addListener { _, _, bounds ->
            center(center)
            mapClip.width = bounds.width
            mapClip.height = bounds.height
        }
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