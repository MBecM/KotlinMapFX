package kotlinmapfx.layer

import javafx.scene.Group
import kotlinmapfx.component.Marker

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