package kotlinmapfx

import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.MapOperations
import javafx.scene.Parent
import javafx.scene.input.MouseButton
import kotlinmapfx.layer.LayeredMap

/**
 * @author Mateusz Becker
 */
interface KotlinOpenStreetMap : MapOperations, LayeredMap {
    fun setCoordinateConsumer(consumerButton: MouseButton, consumer: (LatLon) -> Unit)
    fun disableCoordinateConsumer()
    fun getView(): Parent
}