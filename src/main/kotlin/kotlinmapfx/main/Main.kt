package kotlinmapfx.main

import kotlinmapfx.coord.LatLon
import kotlinmapfx.AbstractKotlinOpenStreetMap
import kotlinmapfx.layer.Layer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.input.MouseButton
import javafx.stage.Stage
import kotlinmapfx.component.AbstractShape
import kotlinmapfx.component.Marker
import kotlinmapfx.component.TestMarker

/**
 * @author Mateusz Becker
 */
class Main : Application() {

    val map = MyMap()
    val marker : Marker = TestMarker(LatLon(54.5745, 18.3908), "")

    override fun start(primaryStage: Stage?) {
        primaryStage?.apply {
            scene = Scene(map.getView(), 500.0, 500.0)
            show()
            map.setCoordinateConsumer(MouseButton.SECONDARY) {
                System.err.println("Latlon:  $it")
                marker.coordinate = it
////                map.markerLayer.removeMarker(marker)
//                map.removeLayer(map.markerLayer)
            }
            map.center(LatLon(54.5745, 18.3908), 16)
//            map.markerLayer.addMarker(marker)
            val shape = AbstractShape()
            shape.markers.add(marker)
            map.markerLayer.addShape(shape)
        }
    }
}

class MyMap() : AbstractKotlinOpenStreetMap() {
    val markerLayer: Layer by layer()
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args);
}