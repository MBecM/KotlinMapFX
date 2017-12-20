package com.mbecm.kotlinmapfx.layer.tile

import com.mbecm.kotlinmapfx.coord.LatLon
import com.mbecm.kotlinmapfx.layer.tile.loader.TileLoader
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import kotlin.math.*

/**
 * @author Mateusz Becker
 */
class DefaultTiledMap : Group(), TiledMap {

    val tiles: Array<MutableMap<Long, Tile>> = Array(20) { i -> mutableMapOf<Long, Tile>() }

    private var maxXForZoom: Long = 0;
    private var maxYForZoom: Long = 0;
    private val tileLoader: TileLoader = TileLoader()

    private val pos = Circle(10.0, Color.BLACK);

    override var zoom: Int = 3
        set(value) {
            field = value
            System.err.println("zoom: " + value)

            maxXForZoom = 1L shl zoom
            maxYForZoom = 1L shl zoom

            loadTiles()
        }

    init {
        loadTiles()
    }

    override fun center(coord: LatLon, zoom: Int) {
        var x = (coord.lon + 180) * (1 shl zoom) / 360.0
        var y = (1 - Math.log(tan(Math.toRadians(coord.lat)) + 1 / cos(Math.toRadians(coord.lat))) / PI) * (1 shl zoom - 1)

        val width: Int = parent?.layoutBounds?.width?.toInt() ?: 0
        val height: Int = parent?.layoutBounds?.height?.toInt() ?: 0

        System.err.println("x: " + x + ", y: " + y)

        pos.translateX = x * -256.0 + (width / 2)
        pos.translateY = y * -256.0 + (height / 2)


        translateX = x * -256.0 + (width / 2)
        translateY = y * -256.0 + (height / 2)
        this.zoom = zoom

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
        translateX = translateX + dx
        translateY = translateY + dy
    }

    override fun loadTiles() {
        children.clear()
        children.add(Circle(15.0, Color.BLACK))

        val width: Int = parent?.layoutBounds?.width?.toInt() ?: 0
        val height: Int = parent?.layoutBounds?.height?.toInt() ?: 0
        val minX = max(0L, abs(-translateX / 256).toLong());
        val maxX = min(maxXForZoom, abs((-translateX + width) / 256).toLong())
        val minY = max(0L, abs(-translateY / 256).toLong())
        val maxY = min(maxYForZoom, abs((-translateY + height) / 256).toLong())

        System.err.println("minX: " + minX + ", maxX: " + maxX);
        System.err.println("minY: " + minY + ", maxY: " + maxY);

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                val tile = tiles[zoom].getOrPut(minX * x * y) { tileLoader.generateTile(zoom, x, y) }
                children.add(tile.apply {
                    translateX = 256 * x.toDouble()
                    translateY = 256 * y.toDouble()
                })
            }
        }
        children.add(pos)
    }

    override fun getView(): Parent {
        return this
    }
}