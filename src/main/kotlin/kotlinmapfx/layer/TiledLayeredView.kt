package kotlinmapfx.layer

import kotlinmapfx.coord.LatLon
import javafx.beans.property.BooleanProperty
import javafx.geometry.Point2D
import javafx.scene.Parent
import kotlinmapfx.layer.tile.TilesProvider

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
    fun screenToLocal(screenX: Double, screenY: Double) : Point2D
}

interface MapOperations {
    val zoom: Int
    val center: LatLon
    fun center(coord: LatLon, zoom: Int = this.zoom)
    fun zoomUp()
    fun zoomDown()
}

interface LayeredMap {
    val tilesProvider: TilesProvider
    fun addLayer(layer: Layer)
    fun removeLayer(layer: Layer)
    fun swapLayers(layer: Layer, layer2: Layer)
}

interface TiledLayeredView : LayeredMap, MovableMap, MapOperations, CoordinateConverter {
    fun getView(): Parent
}