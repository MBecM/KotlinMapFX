package kotlinmapfx.layer.controller

import kotlinmapfx.coord.LatLon
import kotlinmapfx.layer.tile.MovableMap
import kotlinmapfx.layer.tile.TiledMap
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent

/**
 * @author Mateusz Becker
 */
abstract class AbstractMapController(override val movableMap: MovableMap) : MapController {

    override var x: Double = 0.0
    override var y: Double = 0.0

    override fun mousePressed(event: MouseEvent) {
        x = event.x
        y = event.y
    }

    override fun mouseDragged(event: MouseEvent) {
        movableMap.shift(event.x - x, event.y - y)
        x = event.x
        y = event.y
    }

    override fun scroll(event: ScrollEvent) {
        movableMap.zoom(event.deltaY, event.x, event.y)
    }
}