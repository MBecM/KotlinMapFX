package kotlinmapfx.layer.tile

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane
import javafx.scene.transform.Scale

/**
 * @author Mateusz Becker
 */
class Tile(val zoom: Int, val x: Long, val y: Long, val image: Image) : StackPane() {
    val imageView = ImageView(image)
    val debugLabel = Label("z: " + zoom + ", x: " + x + ", y: " + y)

    val scale = Scale()

    init {
//        style = "-fx-border-color: black; -fx-border-size:1;"
        children.add(imageView)
//        children.add(debugLabel)
        scale.pivotX = 0.0
        scale.pivotY = 0.0
        transforms += scale
    }

    override fun toString(): String {
        return "Tile = z: " + zoom + ", x: " + x + ", y: " + y
    }
}