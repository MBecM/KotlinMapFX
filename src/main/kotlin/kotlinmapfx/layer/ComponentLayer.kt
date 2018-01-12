package kotlinmapfx.layer

import javafx.beans.value.ChangeListener
import javafx.scene.Group
import kotlinmapfx.component.Marker
import kotlinmapfx.component.Route
import kotlinmapfx.component.Shape
import kotlinmapfx.coord.LatLon

/**
 * @author Mateusz Becker
 */
class ComponentLayer(override val coordinateConverter: CoordinateConverter) : Group(), Layer {

    private val markers = mutableListOf<Marker>()
    private val listeners = mutableMapOf<Marker, ChangeListener<LatLon>>()

    private val shapes = mutableListOf<Shape>()

    override fun addMarker(marker: Marker) {
        markers += marker
        children += marker.getView()
        refreshMarker(marker)

        val listener = ChangeListener<LatLon> { _, _, _ ->
            refreshMarker(marker)
        }
        marker.coordinateProperty.addListener(listener)
        listeners.put(marker, listener)
    }

    override fun removeMarker(marker: Marker) {
        markers -= marker
        children -= marker.getView()
        listeners.remove(marker).let {
            marker.coordinateProperty.removeListener(it)
        }
    }

    override fun addShape(shape: Shape) {
        children += shape.getView()
        shapes += shape
        shape.anchors.forEach { refreshMarker(it) }
    }


    override fun removeShape(shape: Shape) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addRoute(route: Route) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeRoute(route: Route) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refresh() {
        markers.forEach { this.refreshMarker(it) }
        shapes.forEach { this.refreshShape(it) }
    }

    private fun refreshMarker(marker: Marker) {
        marker.refresh(coordinateConverter.getLocalCoordinate(marker.coordinate))
    }

    private fun refreshShape(shape: Shape) {
        shape.anchors.forEach {
            refreshMarker(it)
        }
    }

    override fun getView(): Group {
        return this
    }
}