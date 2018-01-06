package kotlinmapfx.layer

import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.tile.MapOperations
import javafx.scene.Parent
import javafx.scene.input.MouseButton

/**
 * @author Mateusz Becker
 */
interface LayeredMap : MapOperations {
    fun setCoordinateConsumer(consumerButton: MouseButton, consumer: (LatLon) -> Unit)
    fun disableCoordinateConsumer()
    fun getView(): Parent
}