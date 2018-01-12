package kotlinmapfx.component

import kotlinmapfx.coord.LatLon
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Point2D
import javafx.scene.Node

/**
 * @author Mateusz Becker
 */
interface Marker {
    val coordinateProperty: SimpleObjectProperty<LatLon>
    var coordinate: LatLon
        get() = coordinateProperty.get()
        set(value) = coordinateProperty.set(value)

    fun refresh(localCoordinate: Point2D)
    fun getView(): Node
}

interface DraggableMarker : Marker {
    val localCoordinateProperty: SimpleObjectProperty<Point2D>
    var localCoordinate: Point2D
        get() = localCoordinateProperty.get()
        set(value) = localCoordinateProperty.set(value)

}