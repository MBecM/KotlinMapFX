package kotlinmapfx.layer.tile

import javafx.beans.property.SimpleObjectProperty

/**
 * @author Mateusz Becker
 */
class TilesProvider(defaultTileType: TileType, val servers: List<TileType>) {
    val selectedTileTypeProperty: SimpleObjectProperty<TileType> = SimpleObjectProperty(defaultTileType)
    var selectedTileType: TileType
        get() = selectedTileTypeProperty.get()
        set(value) = selectedTileTypeProperty.set(value)

}

class TileType(val url: String, val name: String) {
    override fun toString(): String {
        return name
    }
}