package kotlinmapfx.component

import javafx.collections.ObservableList
import javafx.scene.Group
import javafx.scene.shape.Path

/**
 * @author Mateusz Becker
 */
interface Shape {
    val shape: Group
    val markers: ObservableList<Marker>
}