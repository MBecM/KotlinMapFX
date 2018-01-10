package kotlinmapfx.component

import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.scene.Group

/**
 * @author Mateusz Becker
 */
class AbstractShape : Group(), Shape {

    override val shape: Group = this
    override val markers: ObservableList<Marker> = FXCollections.observableArrayList()

    init {
        markers.addListener { c: ListChangeListener.Change<out Marker> ->
            while (c.next()) {
                if (c.wasAdded()) {
                    c.addedSubList.forEach {
                        children.add(it.getView())
                    }
                }
                if (c.wasRemoved()) {
                    c.removed.forEach { children -= it.getView() }
                }
            }
        }
    }
}