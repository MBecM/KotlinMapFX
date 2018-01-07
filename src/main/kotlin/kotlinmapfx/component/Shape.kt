package kotlinmapfx.component

import javafx.scene.shape.Path

/**
 * @author Mateusz Becker
 */
interface Shape {
    fun getShape(): Path
    fun clear()
}