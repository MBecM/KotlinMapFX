package kotlinmapfx.layer.tile

import kotlinmapfx.coord.LatLon
import javafx.beans.property.BooleanProperty
import javafx.geometry.Point2D
import javafx.scene.Parent

/**
 * @author Mateusz Becker
 */

interface MovableMap {
    fun shift(dx: Double, dy: Double)
    fun zoom(delta: Double, x: Double, y: Double)
}

interface CoordinateConverter {
    fun getCoordinate(x: Double, y: Double): LatLon
    fun getLocalCoordinate(coord: LatLon): Point2D
}

interface MapOperations  {
    var zoom: Int
    fun center(coord: LatLon, zoom: Int = this.zoom)
}

interface TiledMap : MovableMap, MapOperations, CoordinateConverter {
    val refresh: BooleanProperty
    fun getView(): Parent
}