package kotlinmapfx.component

import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.PathElement
import kotlinmapfx.coord.LatLon

/**
 * @author Mateusz Becker
 */
interface Anchor : Marker {
    val pathElement: PathElement
    var editable: Boolean
    var color: Color
}

class CircleAnchor(val coord: LatLon, override val pathElement: PathElement = LineTo(), radius: Double = 7.0, color: Color = Color.BLACK) : Circle(radius, color), Anchor {

    override var color: Color = color
        set(value) {
            field = value
            fill = value
        }
    override val coordinateProperty: SimpleObjectProperty<LatLon> = SimpleObjectProperty<LatLon>(coord)
    override var editable: Boolean = false
        set(value) {
            field = value
            isVisible = value
        }

    init {
        isVisible = editable
    }

    override fun refresh(localCoordinate: Point2D) {
        when (pathElement) {
            is MoveTo -> {
                pathElement.x = localCoordinate.x
                pathElement.y = localCoordinate.y
            }
            is LineTo -> {
                pathElement.x = localCoordinate.x
                pathElement.y = localCoordinate.y
            }
        }
        translateX = localCoordinate.x
        translateY = localCoordinate.y
    }

    override fun getView(): Node = this

}