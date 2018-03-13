package kotlinmapfx.component

import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.ClosePath
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import kotlinmapfx.coord.LatLon

/**
 * @author Mateusz Becker
 */
interface Shape<out T> where T : Anchor {
    val anchors: List<T>
    var editable: Boolean
    var size: Double
    var color: Color
    var colorOpacity: Double

    fun getView(): Node
}

abstract class AbstractShape<T> : Group(), Shape<T> where T : Anchor {

    protected val shape: Path = Path()
    protected val mutableAnchors: ObservableList<T> = FXCollections.observableArrayList()
    override val anchors: List<T>
        get() {
            return mutableAnchors.toList()
        }
    override var editable: Boolean = false
        set(value) {
            field = value
            anchors.forEach { it.editable = value }
        }
    override var size: Double = 2.0
        set(value) {
            field = value
            shape.strokeWidth = value
        }
    override var color: Color = Color.BLACK
        set(value) {
            field = value
            shape.fill = Color.color(value.red, value.green, value.blue, colorOpacity)
        }
    override var colorOpacity: Double = 0.0
        set(value) {
            field = value
            shape.fill = Color.color(color.red, color.green, color.blue, value)
        }

    init {
        children += shape
        shape.strokeWidth = size
        shape.fill = Color.color(color.red, color.green, color.blue, colorOpacity)

//        mutableAnchors.addListener { c: ListChangeListener.Change<out Anchor> ->
//            while (c.next()) {
//                if (c.wasAdded()) {
//
//                }
//                if (c.wasRemoved()) {
//                    c.removed.forEach {
//                        children -= it.getView()
//                        shape.elements -= it.pathElement
////                        anchors += it
//                    }
//                }
//            }
//        }

    }

    override fun getView(): Node = this
}

class PolygonShape(positions: List<LatLon>) : AbstractShape<CircleAnchor>() {
    private val closePath = ClosePath()
    var closeable: Boolean = false
        set(value) {
            field = value
            if (value) shape.elements += closePath else shape.elements -= closePath
        }

    private var anchorFactory: (index: Int, latLon: LatLon) -> CircleAnchor = { index, latLon ->
        if (index == 0) {
            CircleAnchor(latLon, MoveTo())
        } else {
            CircleAnchor(latLon)
        }
    }

    init {
        positions.mapIndexed(anchorFactory).forEach {
            children += it.getView()
            shape.elements += it.pathElement
            mutableAnchors += it
        }
        if (closeable) {
            shape.elements += ClosePath()
        }
    }

}