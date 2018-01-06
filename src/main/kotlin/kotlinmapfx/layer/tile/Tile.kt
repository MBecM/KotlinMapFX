package kotlinmapfx.layer.tile

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane

/**
 * @author Mateusz Becker
 */
class Tile(val zoom: Int, val x: Long, val y: Long, val image: Image) : StackPane() {
    val imageView = ImageView(image)
    val debugLabel = Label("z: " + zoom + ", x: " + x + ", y: " + y)

    init {
        style = "-fx-border-color: black; -fx-border-size:1;"
        children.add(imageView)
        children.add(debugLabel)
    }

    override fun toString(): String {
        return "Tile = z: " + zoom + ", x: " + x + ", y: " + y
    }
}