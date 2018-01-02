package com.mbecm.kotlinmapfx.layer

import com.mbecm.kotlinmapfx.coord.LatLon
import com.mbecm.kotlinmapfx.layer.controller.DefaultMapController
import com.mbecm.kotlinmapfx.layer.controller.MapController
import com.mbecm.kotlinmapfx.layer.tile.DefaultTiledMap
import com.mbecm.kotlinmapfx.layer.tile.TiledMap
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

/**
 * @author Mateusz Becker
 */
class DefaultLayeredMap(tiledMap: TiledMap = DefaultTiledMap(), val controller: MapController = DefaultMapController(tiledMap)) : Region(), LayeredMap {
    val circle   = Circle(20.0, Color.BLACK)
    val pos = LatLon(54.5745,18.3908)
val l = Pane().apply {
    style = "-fx-background-color: black;"
}
    init {
        children.add(tiledMap.getView())
        children += l
        children.add(TextField("54.5745,18.3908").apply {
            setOnAction {
                tiledMap.center(LatLon(this.text.substringBefore(",").toDouble(), this.text.substringAfter(",").toDouble()),16)
            }
        })

        l.children+= circle

        addEventHandler(MouseEvent.MOUSE_PRESSED, controller::mousePressed)
        addEventHandler(MouseEvent.MOUSE_DRAGGED, controller::mouseDragged)
        addEventHandler(ScrollEvent.SCROLL, controller::scroll)

        tiledMap.refresh.addListener { _,_,_ ->
            val point = tiledMap.getLocalCoordinate(pos)
            System.err.println("refresh: ${point.x}, ${point.y}")
            circle.translateX = point.x
            circle.translateY = point.y
        }
    }

    override fun getView(): Parent {
        return this
    }
}