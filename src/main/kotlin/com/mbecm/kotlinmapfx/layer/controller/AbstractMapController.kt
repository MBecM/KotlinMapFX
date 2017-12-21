package com.mbecm.kotlinmapfx.layer.controller

import com.mbecm.kotlinmapfx.coord.LatLon
import com.mbecm.kotlinmapfx.layer.tile.TiledMap
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent

/**
 * @author Mateusz Becker
 */
abstract class AbstractMapController(tiledMap: TiledMap) : MapController {

    override var x: Double = 0.0
    override var y: Double = 0.0

    init {
    }

    override fun mousePressed(event: MouseEvent) {
        x = event.x
        y = event.y
        if (event.button == MouseButton.SECONDARY) {
//                tiledMap.center(LatLon(54.5745, 18.3908), 16)
//            System.err.println(event.x.toString() + "  " + event.y)
//            System.err.println(tiledMap.getCoordinate(event.x, event.y))
        }

        if (event.button == MouseButton.PRIMARY) {
//            tiledMap.center(LatLon(54.5745, 18.3908), 15)
        }
    }

    override fun mouseDragged(event: MouseEvent) {
        tiledMap.shift( event.x - x,  event.y  -y)
        x = event.x
        y = event.y
    }

    override fun scroll(event: ScrollEvent) {
        tiledMap.zoom(event.deltaY, event.x,event.y)
//        var zoom = tiledMap.zoom;
//        if (event.deltaY > 0) {
//            zoom++;
//            if (zoom > 18) {
//                zoom = 18
//            }
//        } else {
//            zoom--;
//            if (zoom < 0) {
//                zoom = 0
//            }
//        }
//        tiledMap.zoom = zoom
    }
}