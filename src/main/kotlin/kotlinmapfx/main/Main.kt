package kotlinmapfx.main

import kotlinmapfx.coord.LatLon
import kotlinmapfx.AbstractKotlinOpenStreetMap
import kotlinmapfx.layer.Layer
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.MouseButton
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlinmapfx.OSM_TILE_URL
import kotlinmapfx.component.*
import kotlinmapfx.layer.tile.TileType
import kotlinmapfx.layer.tile.TilesProvider
import mu.KotlinLogging

private val log = KotlinLogging.logger { }

/**
 * @author Mateusz Becker
 */
class Main : Application() {

    val map: MyMap
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
            root.right = VBox(Button("Button on right").apply {
                setOnAction {
                    map.tilesProvider.selectedTileType = map.tilesProvider.servers[0]
                }
            }, ComboBox<TileType>(FXCollections.observableArrayList(map.tilesProvider.servers)).apply {
                value = map.tilesProvider.selectedTileType
                setOnAction {
                    map.tilesProvider.selectedTileType = this.value
                }
            })
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

    init {
        val defaultType = TileType(OSM_TILE_URL, "OpenStreetMap")
        val stamenWatercolorType = TileType("http://c.tile.stamen.com/watercolor/", "StamenWatercolor")
        val stamenTonerType = TileType("http://a.tile.stamen.com/toner/", "StamenToner")
        val transportType = TileType("http://a.tile2.opencyclemap.org/transport/", "Transport")
        val wikimediaType = TileType("https://maps.wikimedia.org/osm-intl/", "Wikimedia")
        map = MyMap(TilesProvider(defaultType, listOf(defaultType, stamenTonerType, stamenWatercolorType, transportType, wikimediaType)))
    }
}

class MyMap(override val tilesProvider: TilesProvider) : AbstractKotlinOpenStreetMap(tilesProvider) {
    val markerLayer: Layer by layer()
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args);
}