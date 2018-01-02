package com.mbecm.kotlinmapfx.layer.tile

import com.mbecm.kotlinmapfx.coord.LatLon
import javafx.beans.property.BooleanProperty
import javafx.geometry.Point2D
import javafx.scene.Parent

/**
 * @author Mateusz Becker
 */
interface TiledMap {
    var zoom: Int
    val refresh: BooleanProperty

    fun getView(): Parent

    fun center(coord: LatLon, zoom: Int = this.zoom)

    fun getCoordinate(x: Double, y: Double): LatLon

    fun getLocalCoordinate(coord: LatLon): Point2D

    fun shift(dx: Double, dy: Double)

    fun zoom(delta: Double, x: Double, y: Double)
}