package kotlinmapfx.layer

import javafx.beans.value.ChangeListener
import javafx.scene.Group
import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.tile.CoordinateConverter

/**
 * @author Mateusz Becker
 */
class MarkerLayer(override val coordinateConverter : CoordinateConverter) : Group(), Layer {

    private val markers = mutableListOf<Marker>()
    private val listeners = mutableMapOf<Marker, ChangeListener<LatLon>>()

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

    override fun refresh() {
        markers.forEach { this.refreshMarker(it) }
    }

    private fun refreshMarker(marker: Marker) {
        val localCoordinate = coordinateConverter.getLocalCoordinate(marker.coordinate)
        marker.getView().translateX = localCoordinate.x + marker.getCorrection().x
        marker.getView().translateY = localCoordinate.y + marker.getCorrection().y
    }

    override fun getView(): Group {
        return this
    }
}