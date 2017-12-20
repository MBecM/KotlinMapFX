package com.mbecm.kotlinmapfx.layer.controller

import com.mbecm.kotlinmapfx.layer.tile.TiledMap
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent

/**
 * @author Mateusz Becker
 */
interface MapController {
    var x: Double
    var y: Double
    val tiledMap: TiledMap
    fun mousePressed(event: MouseEvent)
    fun mouseDragged(event: MouseEvent)
    fun scroll(event: ScrollEvent)

}