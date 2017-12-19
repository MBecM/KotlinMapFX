package com.mbecm.kotlinmapfx.layer

import com.mbecm.kotlinmapfx.coord.LatLon
import com.mbecm.kotlinmapfx.layer.tile.TiledMap
import javafx.scene.Parent
import javafx.scene.input.MouseButton
import javafx.scene.layout.Region

/**
 * @author Mateusz Becker
 */
class DefaultLayeredMap(tiledMap: TiledMap) : Region(), LayeredMap {
    var x = 0.0
    var y = 0.0

    init {
        children.add(tiledMap.getView())
        setOnMousePressed {
            x = it.sceneX - tiledMap.getView().translateX
            y = it.sceneY - tiledMap.getView().translateY

            if (it.button == MouseButton.SECONDARY) {
//                tiledMap.center(LatLon(54.5745, 18.3908), 16)
                System.err.println(it.x.toString() + "  " + it.y)
                System.err.println(tiledMap.getCoordinate(it.x, it.y))
            }

            if (it.button == MouseButton.PRIMARY) {
                tiledMap.center(LatLon(54.5745, 18.3908), 15)
            }
        }
        setOnMouseDragged {
            tiledMap.getView().translateX = it.sceneX - x
            tiledMap.getView().translateY = it.sceneY - y
//            translateX = x - translateX
//            x = event.x
        }

        setOnScroll {
            var zoom = tiledMap.zoom;
            if (it.deltaY > 0) {
                zoom++;
                if (zoom > 18) {
                    zoom = 18
                }
            } else {
                zoom--;
                if (zoom < 0) {
                    zoom = 0
                }
            }
            tiledMap.zoom = zoom
//            tiles.putIfAbsent(zoom, mutableMapOf())
//            loadTiles()
        }
    }

    override fun getView(): Parent {
        return this
    }
}