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

    val tiles: Array<MutableMap<Long, MutableMap<Long, Tile>>> = Array(20) { i -> mutableMapOf<Long, MutableMap<Long, Tile>>() }

    private val overlap = -1
    private var maxXForZoom: Long = 0
    private var maxYForZoom: Long = 0
    private val tileLoader: TileLoader = TileLoader()

    private var minX = 0L
    private var maxX = 0L
    private var minY = 0L
    private var maxY = 0L

    private val pos = Circle(10.0, Color.BLACK)

    override var zoom: Int = 3
        set(value) {
            if (field != value) {
                field = value

                System.err.println("zoom: $value")

//                children.clear()
//                loadTiles()
            }
            maxXForZoom = 1L shl zoom
            maxYForZoom = 1L shl zoom
        }

    init {
        loadTiles()
    }

    override fun center(coord: LatLon, zoom: Int) {
        this.zoom = zoom

        var x = (coord.lon + 180) * (1 shl zoom) / 360.0
        var y = (1 - Math.log(tan(Math.toRadians(coord.lat)) + 1 / cos(Math.toRadians(coord.lat))) / PI) * (1 shl zoom - 1)

        val width: Int = parent?.layoutBounds?.width?.toInt() ?: 0
        val height: Int = parent?.layoutBounds?.height?.toInt() ?: 0

//        System.err.println("x: $x, y: $y")

        pos.translateX = x * -256.0 + (width / 2)
        pos.translateY = y * -256.0 + (height / 2)


        translateX = x * -256.0 + (width / 2)
        translateY = y * -256.0 + (height / 2)
        loadTiles()
    }

    override fun zoom(delta: Double, x: Double, y: Double) {
        val latlon = getCoordinate(x, y)
        val newZoom = if (delta > 0) zoom + 1 else zoom - 1
        center(latlon, newZoom)
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

    override fun loadTiles() {
//        children.clear()
//        children.add(Circle(15.0, Color.BLACK))

        val width: Int = parent?.layoutBounds?.width?.toInt() ?: 0
        val height: Int = parent?.layoutBounds?.height?.toInt() ?: 0
        val newMinX = max(0L, abs(-translateX / 256).toLong() - overlap)
        val newMaxX = min(maxXForZoom, abs((-translateX + width) / 256).toLong() + overlap)
        val newMinY = max(0L, abs(-translateY / 256).toLong() - overlap)
        val newMaxY = min(maxYForZoom, abs((-translateY + height) / 256).toLong() + overlap)

        if (newMinX != minX || newMinY != minY) {
            System.err.println("minX: $minX, maxX: $maxX")
            System.err.println("minY: $minY, maxY: $maxY")
            System.err.println("newMinX: $newMinX, newMaxX: $newMaxX")
            System.err.println("newMinY: $newMinY, newMaxY: $newMaxY")

            if (maxX < newMinX || newMaxX < minX) {

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
                    for (y in newMinY..newMaxY) {
                        removeTile(x, y)
//                        System.err.println("REMOVE x: $x , $y")
                    }
                }
                for (x in newMaxX + 1..maxX) {
                    for (y in newMinY..newMaxY) {
                        removeTile(x, y)
//                        System.err.println("REMOVE x2: $x , $y")
                    }
                }

                var deltaMinX = 0
                var deltaMaxX = 0
//                if (newMinX > minX) {
//                    deltaMinX = -1
//                } else if (newMinX < minX) {
//                    deltaMaxX = -1
//                }
                //delete y
                for (x in newMinX + deltaMinX..newMaxX + deltaMaxX) {
                    for (y in minY..newMinY - 1) {
                        removeTile(x, y)
                        System.err.println("REMOVE y: $x , $y")
                    }
                }
                for (x in newMinX + deltaMinX..newMaxX + deltaMaxX) {
                    for (y in newMaxY + 1..maxY) {
                        removeTile(x, y)
                        System.err.println("REMOVE y2: $x , $y")
                    }
                }

                //add x
                for (x in maxX + 1..newMaxX) {
                    for (y in newMinY..newMaxY) {
                        addTile(x, y)
//                        System.err.println("ADD: $x , $y")
                    }
                }
                for (x in newMinX..minX - 1) {
                    for (y in newMinY..newMaxY) {
                        addTile(x, y)
//                        System.err.println("ADD2: $x , $y")
                    }
                }
                deltaMinX = 0
                deltaMaxX = 0
//                if (newMinX > minX) {
//                    deltaMinX = 1
//                } else if (newMinX < minX) {
//                    deltaMaxX = 1
//                }

                //add y
                for (x in newMinX + deltaMinX..newMaxX + deltaMaxX) {
                    for (y in maxY + 1..newMaxY) {
                        addTile(x, y)
                        System.err.println("ADD y: $x , $y")
                    }
                }
                for (x in newMinX + deltaMinX..newMaxX + deltaMaxX) {
                    for (y in newMinY..minY - 1) {
                        addTile(x, y)
                        System.err.println("ADD y2: $x , $y")
                    }
                }
            }


//        }

            minX = newMinX
            maxX = newMaxX
            minY = newMinY
            maxY = newMaxY
        }
//        children.add(pos)
    }

    private fun addTile(x: Long, y: Long) {
        val tile = tiles[zoom].getOrPut(x) { mutableMapOf() }.getOrPut(y) { tileLoader.generateTile(zoom, x, y) }
        children.add(tile.apply {
            translateX = 256 * x.toDouble()
            translateY = 256 * y.toDouble()
        })
    }

    private fun removeTile(x: Long, y: Long) {
        val tile = tiles[zoom].getOrPut(x) { mutableMapOf() }.getOrPut(y) { tileLoader.generateTile(zoom, x, y) }
        children.remove(tile)
    }

    override fun getView(): Parent {
        return this
    }
}