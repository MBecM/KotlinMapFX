package com.mbecm.kotlinmapfx.layer

import com.mbecm.kotlinmapfx.coord.LatLon
import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Point2D
import javafx.scene.Node

/**
 * @author Mateusz Becker
 */
interface Marker {
    val coordinateProperty: SimpleObjectProperty<LatLon>
    var coordinate: LatLon
        get() = coordinateProperty.get()
        set(value) = coordinateProperty.set(value)

    fun getCorrection() : Point2D
    fun getView(): Node
}