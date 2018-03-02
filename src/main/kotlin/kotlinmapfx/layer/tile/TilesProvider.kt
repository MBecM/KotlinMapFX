package kotlinmapfx.layer.tile

import javafx.beans.property.SimpleObjectProperty
import kotlinmapfx.OSM_TILE_URL
import mu.KotlinLogging

private val log = KotlinLogging.logger { }

/**
 * @author Mateusz Becker
 */
abstract class TilesProvider(defaultTileType: TileType) {
    abstract val servers: List<TileType>
    abstract val attribution: String
    val selectedTileTypeProperty: SimpleObjectProperty<TileType> = SimpleObjectProperty(defaultTileType)
    var selectedTileType: TileType
        get() = selectedTileTypeProperty.get()
        set(value) = selectedTileTypeProperty.set(value)

}

class SimpleOSMTilesProvider(
        defaultTileType: TileType = TileType(OSM_TILE_URL, "OpenStreetMap"),
        override val servers: List<TileType> = listOf(defaultTileType),
        override val attribution: String = "© OpenStreetMap contributors") : TilesProvider(defaultTileType)

class ExtendedTilesProvider(
        defaultTileType: TileType = TileType(OSM_TILE_URL, "OpenStreetMap"),
        override val attribution: String = "© OpenStreetMap contributors") : TilesProvider(defaultTileType) {
    override val servers: List<TileType>

    init {
        val stamenWatercolorType = TileType("http://c.tile.stamen.com/watercolor/", "StamenWatercolor")
        val stamenTonerType = TileType("http://a.tile.stamen.com/toner/", "StamenToner")
        val transportType = TileType("http://a.tile2.opencyclemap.org/transport/", "Transport")
        val wikimediaType = TileType("https://maps.wikimedia.org/osm-intl/", "Wikimedia")
        servers = listOf(defaultTileType, stamenTonerType, stamenWatercolorType, transportType, wikimediaType)
    }
}

class TileType(val url: String, val name: String) {
    override fun toString(): String {
        return name
    }
}