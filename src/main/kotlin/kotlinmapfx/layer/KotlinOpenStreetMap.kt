package kotlinmapfx.layer

import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.tile.MapOperations
import javafx.scene.Parent
import javafx.scene.input.MouseButton
import kotlinmapfx.layer.tile.LayeredMap

/**
 * @author Mateusz Becker
 */
interface KotlinOpenStreetMap : MapOperations, LayeredMap {
    fun setCoordinateConsumer(consumerButton: MouseButton, consumer: (LatLon) -> Unit)
    fun disableCoordinateConsumer()
    fun getView(): Parent
}