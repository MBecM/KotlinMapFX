package kotlinmapfx.controller

import kotlinmapfx.layer.MovableMap
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent

/**
 * @author Mateusz Becker
 */
interface MapController {
    var x: Double
    var y: Double
    val movableMap: MovableMap
    fun mousePressed(event: MouseEvent)
    fun mouseDragged(event: MouseEvent)
    fun scroll(event: ScrollEvent)
}