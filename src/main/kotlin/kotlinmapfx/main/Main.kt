package kotlinmapfx.main

import kotlinmapfx.coord.LatLon
import kotlinmapfx.AbstractKotlinOpenStreetMap
import kotlinmapfx.layer.Layer
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlinmapfx.component.*
import mu.KotlinLogging

private val log = KotlinLogging.logger {  }

/**
 * @author Mateusz Becker
 */
class Main : Application() {

    val map = MyMap()
    val marker: Marker = TestMarker(LatLon(54.574534565, 18.3908765443), "")
    val shape = PolygonShape()
    override fun start(primaryStage: Stage?) {
        primaryStage?.apply {

            val root = BorderPane()
            root.top = Label("Title: KotlinMapFX")
            root.center = map.getView()
            root.left = TextField("54.574534565, 18.3908765443").apply {
                setOnAction {
                    map.center(LatLon(this.text.substringBefore(",").toDouble(), this.text.substringAfter(",").toDouble()), 16)
                }
            }
            root.bottom = Label("Status: ONLINE")
            root.right = Button("Button on right")
            scene = Scene(root, 900.0, 600.0)
            show()
            map.setCoordinateConsumer(MouseButton.SECONDARY) {
                log.debug("Latlon:  $it")
                marker.coordinate = it
////                map.markerLayer.removeMarker(marker)
//                map.removeLayer(map.markerLayer)
                shape.editable = !shape.editable
                shape.color = if (shape.color == Color.BLACK) Color.GREEN else Color.BLACK
                shape.colorOpacity = 0.5
            }
            map.center(LatLon(54.574534565, 18.3908765443), 16)

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