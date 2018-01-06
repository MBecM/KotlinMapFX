package com.mbecm.kotlinmapfx.layer

import com.mbecm.kotlinmapfx.coord.LatLon
import com.mbecm.kotlinmapfx.layer.controller.DefaultMapController
import com.mbecm.kotlinmapfx.layer.controller.MapController
import com.mbecm.kotlinmapfx.layer.tile.DefaultTiledMap
import com.mbecm.kotlinmapfx.layer.tile.MapOperations
import com.mbecm.kotlinmapfx.layer.tile.TiledMap
import javafx.scene.Parent
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Region
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author Mateusz Becker
 */
abstract class DefaultLayeredMap(private val tiledMap: TiledMap = DefaultTiledMap(), val controller: MapController = DefaultMapController(tiledMap)) : Region(), LayeredMap, MapOperations by tiledMap {

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

    protected fun layer(order: Double = 0.0): ReadOnlyProperty<DefaultLayeredMap, Layer> = object : ReadOnlyProperty<DefaultLayeredMap, Layer> {
        override fun getValue(thisRef: DefaultLayeredMap, property: KProperty<*>): Layer {
            return MarkerLayer(tiledMap)
        }
    }
}