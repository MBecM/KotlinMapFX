package kotlinmapfx.main

import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.AbstractKotlinOpenStreetMap
import kotlinmapfx.layer.Layer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import kotlinmapfx.layer.TestMarker

/**
 * @author Mateusz Becker
 */
class Main : Application() {

    val map = MyMap()
    val marker = TestMarker(LatLon(54.5745, 18.3908), "")

    override fun start(primaryStage: Stage?) {
        primaryStage?.apply {
            scene = Scene(map.getView(), 500.0, 500.0)
            show()
            map.setCoordinateConsumer(MouseButton.SECONDARY) {
                System.err.println("Latlon:  $it")
//                map.markerLayer.removeMarker(marker)
                map.removeLayer(map.markerLayer)
            }
            map.center(LatLon(54.5745, 18.3908), 16)
            map.markerLayer.addMarker(marker)
            map.markerLayer
        }
    }
}

class MyMap() : AbstractKotlinOpenStreetMap() {
    val markerLayer: Layer by layer()
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args);
}