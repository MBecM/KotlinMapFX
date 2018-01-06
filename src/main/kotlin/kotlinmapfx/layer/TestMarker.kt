package kotlinmapfx.layer

import kotlinmapfx.coord.LatLon
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.control.Button

/**
 * @author Mateusz Becker
 */
class TestMarker(coord: LatLon, text: String) : Button(text), Marker {

    init {
        style = "-fx-shape: \"M13.558,0C6.07,0,0,6.07,0,13.558s13.558,22.346,13.558,22.346s13.558-14.858,13.558-22.346S21.045,0,13.558,0zM13.558,20.762c-4.084,0-7.395-3.311-7.395-7.395s3.311-7.395,7.395-7.395c4.084,0,7.396,3.311,7.396,7.395S17.642,20.762,13.558,20.762z\"; \n" +
                "-fx-pref-height: 48;\n" +
                "-fx-pref-width: 35; \n" +
                "-fx-border-color: black;\n" +
                "-fx-border-width: 0.5;\n" +
                "-fx-background-color: red;\n" +
                "-fx-effect: dropshadow(two-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 1 , 1);"
    }

    override val coordinateProperty: SimpleObjectProperty<LatLon> = SimpleObjectProperty(coord)

    override fun getCorrection(): Point2D {
        return Point2D(-this.width / 2, -this.height)
    }

    override fun getView(): Node {
        return this
    }
}