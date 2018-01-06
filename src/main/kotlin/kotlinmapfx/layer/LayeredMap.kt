package com.mbecm.kotlinmapfx.layer

import com.mbecm.kotlinmapfx.coord.LatLon
import com.mbecm.kotlinmapfx.layer.tile.MapOperations
import javafx.scene.Parent
import javafx.scene.input.MouseButton

/**
 * @author Mateusz Becker
 */
interface LayeredMap : MapOperations {
    fun setCoordinateConsumer(consumerButton: MouseButton, consumer: (LatLon) -> Unit)
    fun disableCoordinateConsumer()
    fun getView(): Parent
}