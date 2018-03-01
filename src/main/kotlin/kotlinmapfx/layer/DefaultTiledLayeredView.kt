package kotlinmapfx.layer

import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.tile.TileLoader
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.Parent
import kotlinmapfx.layer.tile.TilesProvider
import mu.KotlinLogging
import kotlin.math.*

private val log = KotlinLogging.logger { }

/**
 * @author Mateusz Becker
 */
class DefaultTiledLayeredView(override val tilesProvider: TilesProvider) : Group(), TiledLayeredView {

    private val overlap = 2
    private var maxXYForZoom: Long = 0
    private val tileLoader: TileLoader


    private var minX = -100L
    private var maxX = -100L
    private var minY = -100L
    private var maxY = -100L

    private val defaultLayersAmount = 1
    private val tilesLayer = Group()
    private val layers = mutableListOf<Layer>()

    override var zoom: Int = 3
        set(value) {
            if (field != value) {
                field = value
                maxXYForZoom = 1L shl field
                log.debug("Current zoom: $value")
                tilesLayer.children.clear()
//                layers.forEach { it.refresh() }
                minX = -100
                maxX = -100
                minY = -100
                maxY = -100
            }
        }
    var zoomTileScale = 0
    var tileScale = 1.0

    private var _center: LatLon? = null
    override val center: LatLon
        get() {
            if (_center == null) {
                _center = LatLon(0.0, 0.0)
            }
            return _center ?: throw AssertionError("Center coordinate cannot be null")
        }

    init {
        tileLoader = TileLoader(tilesProvider) {
            tilesLayer.children.clear()
            minX = -100
            maxX = -100
            minY = -100
            maxY = -100
            loadTiles()
        }
        children += tilesLayer
        loadTiles()
    }

    override fun center(coord: LatLon, zoom: Int) {
        this.zoom = zoom
        val localPoint = getLocalCoordinate(coord)
        val width: Double = parent?.layoutBounds?.width ?: 0.0
        val height: Double = parent?.layoutBounds?.height ?: 0.0
        translateX = -localPoint.x + (width / 2)
        translateY = -localPoint.y + (height / 2)
        loadTiles()
    }

    override fun zoom(delta: Double, x: Double, y: Double) {
        val latlon = getCoordinate(x, y)
        if (delta > 0) {
            zoomTileScale++
            if (zoomTileScale == 4) {
                zoom++
                zoomTileScale = 0
            }
            tileScale = 1 + zoomTileScale * 0.25
        } else {
            zoomTileScale--
            if (zoomTileScale < 0) {
                zoom--
                zoomTileScale = 3
            }
            tileScale = 1 + zoomTileScale * 0.25
        }
        layers.forEach { it.refresh() }
        val point = getLocalCoordinate(latlon)
        translateX = -point.x + x
        translateY = -point.y + y
        loadTiles()
    }

    override fun getLocalCoordinate(coord: LatLon): Point2D {
        val x = (coord.lon + 180) * maxXYForZoom / 360.0
        val y = (1 - Math.log(tan(Math.toRadians(coord.lat)) + 1 / cos(Math.toRadians(coord.lat))) / PI) * (1 shl zoom - 1)
        return Point2D(x * (256 * tileScale), y * (256 * tileScale))
    }

    override fun getCoordinate(x: Double, y: Double): LatLon {
        val tx = (translateX - x) / (-256.0 * tileScale)
        val ty = (translateY - y) / (-256.0 * tileScale)
        val lat = Math.toDegrees(atan(sinh(PI - (ty * 2 * PI) / maxXYForZoom)))
        val lon = (tx * 360 / maxXYForZoom) - 180
        return LatLon(lat, lon)
    }

    override fun screenToLocal(screenX: Double, screenY: Double): Point2D {
        return parent.screenToLocal(screenX, screenY)
    }

    override fun shift(dx: Double, dy: Double) {
        translateX += dx
        translateY += dy
        loadTiles()
    }

    private fun loadTiles() {
        val width: Double = parent?.layoutBounds?.width ?: 0.0
        val height: Double = parent?.layoutBounds?.height ?: 0.0
        val newMinX = max(0L, abs(-translateX / (256 * tileScale)).toLong() - overlap)
        val newMaxX = min(maxXYForZoom, abs((-translateX + width) / (256 * tileScale)).toLong() + overlap)
        val newMinY = max(0L, abs(-translateY / (256 * tileScale)).toLong() - overlap)
        val newMaxY = min(maxXYForZoom, abs((-translateY + height) / (256 * tileScale)).toLong() + overlap)

        _center = getCoordinate(width / 2, height / 2)

        if (newMinX != minX || newMinY != minY || newMaxX != maxX || newMaxY != maxY) {
            log.trace("===========================================")
            log.trace("minX: $minX, maxX: $maxX")
            log.trace("minY: $minY, maxY: $maxY")
            log.trace("newMinX: $newMinX, newMaxX: $newMaxX")
            log.trace("newMinY: $newMinY, newMaxY: $newMaxY")

            if (maxX < newMinX || newMaxX < minX || maxY < newMinY || newMaxY < minY) {

                for (x in minX..maxX) {
                    for (y in minY..maxY) {
                        removeTile(x, y)
                        log.trace("REMOVE CLEAR: $x , $y")
                    }
                }
                for (x in newMinX..newMaxX) {
                    for (y in newMinY..newMaxY) {
                        addTile(x, y)
                        log.trace("ADD CLEAR: $x , $y")
                    }
                }
            } else {

                //delete x
                for (x in minX..newMinX - 1) {
                    for (y in min(minY, newMinY)..max(maxY, newMaxY)) {
                        removeTile(x, y)
                        log.trace("REMOVE x: $x , $y")
                    }
                }
                for (x in newMaxX + 1..maxX) {
                    for (y in min(minY, newMinY)..max(maxY, newMaxY)) {
                        removeTile(x, y)
                        log.trace("REMOVE x2: $x , $y")
                    }
                }

                //delete y
                for (x in min(minX, newMinX)..max(maxX, newMaxX)) {
                    for (y in minY..newMinY - 1) {
                        removeTile(x, y)
                        log.trace("REMOVE y: $x , $y")
                    }
                }
                for (x in min(minX, newMinX)..max(maxX, newMaxX)) {
                    for (y in newMaxY + 1..maxY) {
                        removeTile(x, y)
                        log.trace("REMOVE y2: $x , $y")
                    }
                }

                //add x
                for (x in maxX + 1..newMaxX) {
                    for (y in newMinY..newMaxY) {
                        addTile(x, y)
                        log.trace("ADD x: $x , $y")
                    }
                }
                for (x in newMinX..minX - 1) {
                    for (y in newMinY..newMaxY) {
                        addTile(x, y)
                        log.trace("ADD x2: $x , $y")
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
                        log.trace("ADD y: $x , $y")
                    }
                }
                //add y - top
                for (x in newMinX + deltaMinX..newMaxX + deltaMaxX) {
                    for (y in newMinY..minY - 1) {
                        addTile(x, y)
                        log.trace("ADD y2: $x , $y")
                    }
                }
            }
            minX = newMinX
            maxX = newMaxX
            minY = newMinY
            maxY = newMaxY
        }

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                val tile = tileLoader.tiles[zoom].getOrPut(x) { mutableMapOf() }.getOrPut(y) { tileLoader.generateTile(zoom, x, y) }
                tile.apply {
                    scale.x = tileScale
                    scale.y = tileScale
                    translateX = 256 * tileScale * x.toDouble()
                    translateY = 256 * tileScale * y.toDouble()
                }
            }
        }
    }

    private fun addTile(x: Long, y: Long) {
        val tile = tileLoader.tiles[zoom].getOrPut(x) { mutableMapOf() }.getOrPut(y) { tileLoader.generateTile(zoom, x, y) }
        tilesLayer.children.add(tile.apply {
            translateX = 256 * tileScale * x.toDouble()
            translateY = 256 * tileScale * y.toDouble()
        })
    }

    private fun removeTile(x: Long, y: Long) {
        val tile = tileLoader.tiles[zoom].getOrPut(x) { mutableMapOf() }.getOrPut(y) { tileLoader.generateTile(zoom, x, y) }
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