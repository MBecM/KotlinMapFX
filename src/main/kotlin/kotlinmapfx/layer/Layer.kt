package kotlinmapfx.layer

import kotlinmapfx.layer.tile.TiledMap
import javafx.scene.Group
import kotlinmapfx.layer.tile.CoordinateConverter

/**
 * @author Mateusz Becker
 */
interface Layer {
    val coordinateConverter : CoordinateConverter
    fun getView():Group
    fun refresh()

    //TODO: only for marker layer, needs to be more generic
    fun addMarker(marker: Marker)
    fun removeMarker(marker: Marker)
}