package kotlinmapfx.layer

import javafx.scene.Group
import kotlinmapfx.component.Marker
import kotlinmapfx.component.Route
import kotlinmapfx.component.Shape

/**
 * @author Mateusz Becker
 */
interface Layer {
    val coordinateConverter : CoordinateConverter
    fun getView():Group
    fun refresh()

    fun addMarker(marker: Marker)
    fun removeMarker(marker: Marker)

    fun addShape(shape: Shape)
    fun removeShape(shape: Shape)

    fun addRoute(route: Route)
    fun removeRoute(route: Route)
}