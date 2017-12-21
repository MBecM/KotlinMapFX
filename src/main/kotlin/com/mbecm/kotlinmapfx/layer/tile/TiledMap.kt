package com.mbecm.kotlinmapfx.layer.tile

import com.mbecm.kotlinmapfx.coord.LatLon
import javafx.scene.Parent

/**
 * @author Mateusz Becker
 */
interface TiledMap {
    var zoom: Int

    fun getView(): Parent

    fun center(coord: LatLon, zoom: Int = this.zoom)

    fun getCoordinate(x: Double, y: Double): LatLon

    fun shift(dx: Double, dy: Double)

    fun zoom(delta: Double, x: Double, y: Double)

    //TODO: just for debuging, will be removed in future
    fun loadTiles()
}