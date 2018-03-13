package kotlinmapfx.layer

import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.MouseEvent
import kotlinmapfx.component.DraggableMarker
import kotlinmapfx.component.Marker
import kotlinmapfx.component.Shape
import kotlinmapfx.coord.LatLon

/**
 * @author Mateusz Becker
 */
class ComponentLayer(override val coordinateConverter: CoordinateConverter) : Group(), Layer {

    private val markers = mutableListOf<Marker>()
    private val coordListeners = mutableMapOf<Marker, ChangeListener<LatLon>>()

    private val localCoordlisteners = mutableMapOf<Marker, ChangeListener<Point2D>>()
    private val mouseReleaseEvents = mutableMapOf<Marker, EventHandler<MouseEvent>>()

    private val shapes = mutableListOf<Shape>()

    override fun addMarker(marker: Marker) {
        markers += marker
        children += marker.getView()
        addMarkerAndListener(marker)
    }

    override fun removeMarker(marker: Marker) {
        markers -= marker
        children -= marker.getView()
        coordListeners.remove(marker)?.let {
            marker.coordinateProperty.removeListener(it)
        }
    }

    private fun addMarkerAndListener(marker: Marker) {
        refreshMarker(marker)

        val listener = ChangeListener<LatLon> { _, _, _ ->
            refreshMarker(marker)
        }
        marker.coordinateProperty.addListener(listener)
        coordListeners.put(marker, listener)
    }

    private fun addDraggableMarkerAndListener(marker: DraggableMarker) {
        addMarkerAndListener(marker)

        val listener = ChangeListener<Point2D> { _, _, newLocalCoord ->
            marker.coordinate = coordinateConverter.getCoordinate(newLocalCoord.x, newLocalCoord.y)
        }
        marker.localCoordinateProperty.addListener(listener)
        localCoordlisteners.put(marker, listener)

        val releaseEvent = EventHandler<MouseEvent> {
            marker.localCoordinate = coordinateConverter.screenToLocal(it.screenX - it.x, it.screenY - it.y)
            it.consume()
        }
        marker.getView().addEventHandler(MouseEvent.MOUSE_RELEASED, releaseEvent)
        mouseReleaseEvents.put(marker, releaseEvent)
    }

    private fun removeDraggableMarkerAndListener(marker: DraggableMarker) {
        coordListeners.remove(marker)?.let {
            marker.coordinateProperty.removeListener(it)
        }
        localCoordlisteners.remove(marker)?.let {
            marker.localCoordinateProperty.removeListener(it)
        }
        mouseReleaseEvents.remove(marker)?.let {
            marker.getView().removeEventHandler(MouseEvent.MOUSE_RELEASED, it)
        }
    }

    override fun addShape(shape: Shape) {
        children += shape.getView()
        shapes += shape
        shape.anchors.forEach {
            addDraggableMarkerAndListener(it)
        }
    }

    override fun removeShape(shape: Shape) {
        children -= shape.getView()
        shapes -= shape
        shape.anchors.forEach {
            removeDraggableMarkerAndListener(it)
        }
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