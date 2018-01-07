package kotlinmapfx.layer.tile

import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.Layer
import kotlinmapfx.layer.MarkerLayer
import kotlinmapfx.layer.TestMarker
import kotlinmapfx.layer.tile.loader.TileLoader
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import kotlin.math.*

/**
 * @author Mateusz Becker
 */
class DefaultTiledMap : Group(), TiledMap {

    val tiles: Array<MutableMap<Long, MutableMap<Long, Tile>>> = Array(20) { _ -> mutableMapOf<Long, MutableMap<Long, Tile>>() }
    val pos = LatLon(54.5745, 18.3908)
    private val overlap = 2
    private var maxXForZoom: Long = 0
    private var maxYForZoom: Long = 0
    private val tileLoader: TileLoader = TileLoader()

    private var minX = -100L
    private var maxX = -100L
    private var minY = -100L
    private var maxY = -100L

    private val defaultLayersAmount = 1
    private val tilesLayer = Group()
    private val layers = mutableListOf<Layer>()

    override val refresh: BooleanProperty = SimpleBooleanProperty(false)
    override var zoom: Int = 3
        set(value) {
            if (field != value) {
                field = value
                System.err.println("zoom: $value")
                tilesLayer.children.clear()
                layers.forEach { it.refresh() }
                minX = -100
                maxX = -100
                minY = -100
                maxY = -100
            }
            maxXForZoom = 1L shl zoom
            maxYForZoom = 1L shl zoom
        }

    init {
        children += tilesLayer
        loadTiles()
    }

    override fun center(coord: LatLon, zoom: Int) {
        this.zoom = zoom
        val localPoint = getLocalCoordinate(coord)
        val width: Int = parent?.layoutBounds?.width?.toInt() ?: 0
        val height: Int = parent?.layoutBounds?.height?.toInt() ?: 0
        translateX = -localPoint.x + (width / 2)
        translateY = -localPoint.y + (height / 2)
        loadTiles()
    }

    override fun zoom(delta: Double, x: Double, y: Double) {
        val latlon = getCoordinate(x, y)
        if (delta > 0) zoom++ else zoom--
        val point = getLocalCoordinate(latlon)
        translateX = -point.x + x
        translateY = -point.y + y
        loadTiles()
    }

    override fun getLocalCoordinate(coord: LatLon): Point2D {
        var x = (coord.lon + 180) * (1 shl zoom) / 360.0
        var y = (1 - Math.log(tan(Math.toRadians(coord.lat)) + 1 / cos(Math.toRadians(coord.lat))) / PI) * (1 shl zoom - 1)
        return Point2D(x * 256, y * 256)
    }

    override fun getCoordinate(x: Double, y: Double): LatLon {
        val n = 1 shl zoom
        val tx = (translateX - x) / -256.0
        val ty = (translateY - y) / -256.0
        val lat = Math.toDegrees(atan(sinh(PI - (ty * 2 * PI) / n)))
        val lon = (tx * 360 / n) - 180
        return LatLon(lat, lon)
    }

    override fun shift(dx: Double, dy: Double) {
        translateX += dx
        translateY += dy
        loadTiles()
    }

    fun loadTiles() {
        refresh.set(true)
        val width: Int = parent?.layoutBounds?.width?.toInt() ?: 0
        val height: Int = parent?.layoutBounds?.height?.toInt() ?: 0
        val newMinX = max(0L, abs(-translateX / 256).toLong() - overlap)
        val newMaxX = min(maxXForZoom, abs((-translateX + width) / 256).toLong() + overlap)
        val newMinY = max(0L, abs(-translateY / 256).toLong() - overlap)
        val newMaxY = min(maxYForZoom, abs((-translateY + height) / 256).toLong() + overlap)

        if (newMinX != minX || newMinY != minY || newMaxX != maxX || newMaxY != maxY) {
            System.err.println("===========================================")
            System.err.println("minX: $minX, maxX: $maxX")
            System.err.println("minY: $minY, maxY: $maxY")
            System.err.println("newMinX: $newMinX, newMaxX: $newMaxX")
            System.err.println("newMinY: $newMinY, newMaxY: $newMaxY")

            if (maxX < newMinX || newMaxX < minX || maxY < newMinY || newMaxY < minY) {

                for (x in minX..maxX) {
                    for (y in minY..maxY) {
                        removeTile(x, y)
                        System.err.println("REMOVE CLEAR: $x , $y")
                    }
                }
                for (x in newMinX..newMaxX) {
                    for (y in newMinY..newMaxY) {
                        addTile(x, y)
                        System.err.println("ADD CLEAR: $x , $y")
                    }
                }
            } else {

                //delete x
                for (x in minX..newMinX - 1) {
                    for (y in min(minY, newMinY)..max(maxY, newMaxY)) {
                        removeTile(x, y)
                        System.err.println("REMOVE x: $x , $y")
                    }
                }
                for (x in newMaxX + 1..maxX) {
                    for (y in min(minY, newMinY)..max(maxY, newMaxY)) {
                        removeTile(x, y)
                        System.err.println("REMOVE x2: $x , $y")
                    }
                }

                //delete y
                for (x in min(minX, newMinX)..max(maxX, newMaxX)) {
                    for (y in minY..newMinY - 1) {
                        removeTile(x, y)
                        System.err.println("REMOVE y: $x , $y")
                    }
                }
                for (x in min(minX, newMinX)..max(maxX, newMaxX)) {
                    for (y in newMaxY + 1..maxY) {
                        removeTile(x, y)
                        System.err.println("REMOVE y2: $x , $y")
                    }
                }

                //add x
                for (x in maxX + 1..newMaxX) {
                    for (y in newMinY..newMaxY) {
                        addTile(x, y)
                        System.err.println("ADD x: $x , $y")
                    }
                }
                for (x in newMinX..minX - 1) {
                    for (y in newMinY..newMaxY) {
                        addTile(x, y)
                        System.err.println("ADD x2: $x , $y")
                    }
                }
                var deltaMinX = 0L
                var deltaMaxX = 0L
                if (newMinX < minX) { //ok
                    deltaMinX = minX - newMinX // 1
                }

                if (newMaxX > maxX) {//ok
                    deltaMaxX = maxX - newMaxX //-1
                }

                //add y - bottom
                for (x in newMinX + deltaMinX..newMaxX + deltaMaxX) {
                    for (y in maxY + 1..newMaxY) {
                        addTile(x, y)
                        System.err.println("ADD y: $x , $y")
                    }
                }
                //add y - top
                for (x in newMinX + deltaMinX..newMaxX + deltaMaxX) {
                    for (y in newMinY..minY - 1) {
                        addTile(x, y)
                        System.err.println("ADD y2: $x , $y")
                    }
                }
            }
            minX = newMinX
            maxX = newMaxX
            minY = newMinY
            maxY = newMaxY
        }

        refresh.set(false)
    }

    private fun addTile(x: Long, y: Long) {
        val tile = tiles[zoom].getOrPut(x) { mutableMapOf() }.getOrPut(y) { tileLoader.generateTile(zoom, x, y) }
        tilesLayer.children.add(tile.apply {
            translateX = 256 * x.toDouble()
            translateY = 256 * y.toDouble()
        })
    }

    private fun removeTile(x: Long, y: Long) {
        val tile = tiles[zoom].getOrPut(x) { mutableMapOf() }.getOrPut(y) { tileLoader.generateTile(zoom, x, y) }
        tilesLayer.children.remove(tile)
    }

    override fun addLayer(layer: Layer) {
        layers += layer
        children += layer.getView()
    }

    override fun removeLayer(layer: Layer) {
        layers -= layer
        children -= layer.getView()
    }

    override fun swapLayers(layer: Layer, layer2: Layer) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getView(): Parent {
        return this
    }
}