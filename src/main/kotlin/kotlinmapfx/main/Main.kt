package kotlinmapfx.main

import kotlinmapfx.coord.LatLon
import kotlinmapfx.AbstractKotlinOpenStreetMap
import kotlinmapfx.layer.Layer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlinmapfx.component.*

/**
 * @author Mateusz Becker
 */
class Main : Application() {

    val map = MyMap()
    val marker: Marker = TestMarker(LatLon(54.5745, 18.3908), "")
    val shape = PolygonShape()
    override fun start(primaryStage: Stage?) {
        primaryStage?.apply {
            scene = Scene(map.getView(), 800.0, 700.0)
            show()
            map.setCoordinateConsumer(MouseButton.SECONDARY) {
                System.err.println("Latlon:  $it")
                marker.coordinate = it
////                map.markerLayer.removeMarker(marker)
//                map.removeLayer(map.markerLayer)
                shape.editable = !shape.editable
                shape.color = if (shape.color == Color.BLACK) Color.GREEN else Color.BLACK
                shape.colorOpacity = 0.5
            }
            map.center(LatLon(54.5745, 18.3908), 16)

            shape.setPositions(listOf(LatLon(54.5745, 18.3908), LatLon(54.6745, 18.2908), LatLon(54.7745, 18.5908)))
            map.markerLayer.addShape(shape)
            map.markerLayer.addMarker(marker)
        }
    }
}

class MyMap() : AbstractKotlinOpenStreetMap() {
    val markerLayer: Layer by layer()
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args);
}