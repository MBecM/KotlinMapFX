package com.mbecm.kotlinmapfx.layer.tile

import com.mbecm.kotlinmapfx.layer.tile.loader.TileLoader
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

/**
 * @author Mateusz Becker
 */
class DefaultTiledMap : Group(), TiledMap {

    val tiles: MutableMap<Int, MutableMap<Long, Image>> = mutableMapOf();

    private var maxXForZoom: Long = 0;
    private var maxYForZoom: Long = 0;
    private val tileLoader: TileLoader = TileLoader()

    override var zoom: Int = 3
        set(value) {
            field = value
            tiles.putIfAbsent(zoom, mutableMapOf())
            System.err.println("zoom: " + value)

            maxXForZoom = 1L shl zoom
            maxYForZoom = 1L shl zoom

            loadTiles()
        }

    init {
        loadTiles()
    }

    override fun loadTiles() {
        children.clear()
        children.add(Circle(15.0, Color.BLACK))
        val width: Int = parent?.layoutBounds?.width?.toInt() ?: 0
        val height: Int = parent?.layoutBounds?.height?.toInt() ?: 0
        val minX = Math.max(0L, Math.abs(-translateX / 256).toLong());
        val maxX = Math.min(maxXForZoom, Math.abs((-translateX + width) / 256).toLong())
        val minY = Math.max(0L, Math.abs(-translateY / 256).toLong())
        val maxY = Math.min(maxYForZoom, Math.abs((-translateY + height) / 256).toLong())

        System.err.println("" + minX + ", " + maxX);

        for (x in minX..maxX) {
            for (y in minY..maxY) {
//                tiles.get(zoom)?.put((x + 10) * (y + 20), img);

                val iv = ImageView(tileLoader.generateTile(zoom, x, y))

                children.add(StackPane(iv, Label("x: " + x + ", y: " + y)).apply {
                    style = "-fx-border-color: black; -fx-border-size:1;"
                    translateX = 256 * x.toDouble()
                    translateY = 256 * y.toDouble()
                })
            }
        }
    }

    override fun getView(): Parent {
        return this
    }
}