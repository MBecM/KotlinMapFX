package kotlinmapfx.component

import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.PathElement
import kotlinmapfx.coord.LatLon

/**
 * @author Mateusz Becker
 */
interface Anchor : DraggableMarker {
    val pathElement: PathElement
    var editable: Boolean
    var color: Color
}

class CircleAnchor(val coord: LatLon, override val pathElement: PathElement = LineTo(), radius: Double = 7.0, color: Color = Color.BLACK) : Circle(radius, color), Anchor {

    private var x: Double = 0.0
    private var y: Double = 0.0

    override var color: Color = color
        set(value) {
            field = value
            fill = value
        }
    override val coordinateProperty: SimpleObjectProperty<LatLon> = SimpleObjectProperty(coord)
    override val localCoordinateProperty: SimpleObjectProperty<Point2D> = SimpleObjectProperty(Point2D(0.0, 0.0))
    override var editable: Boolean = false
        set(value) {
            field = value
            isVisible = value
        }

    init {
        isVisible = editable

        addEventHandler(MouseEvent.MOUSE_PRESSED) {
            x = it.sceneX
            y = it.sceneY
            it.consume()
        }

        addEventHandler(MouseEvent.MOUSE_DRAGGED) {
            refresh(Point2D(translateX + (it.sceneX - x), translateY + (it.sceneY - y)))
            x = it.sceneX
            y = it.sceneY
            it.consume()
        }

        addEventHandler(MouseEvent.MOUSE_RELEASED) {
            //          val d=  parent.screenTo(Point2D(it.screenX, it.screenY))
            localCoordinate = Point2D(it.sceneX, it.sceneY)
            it.consume()
            TODO("local coordinate by scene but should be by map size.")
        }

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