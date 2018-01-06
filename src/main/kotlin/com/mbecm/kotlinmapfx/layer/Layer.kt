package com.mbecm.kotlinmapfx.layer

import com.mbecm.kotlinmapfx.layer.tile.TiledMap
import javafx.scene.Group

/**
 * @author Mateusz Becker
 */
interface Layer {
    val tiledMap : TiledMap
    fun getView():Group
    fun refresh()

    //TODO: only for marker layer, needs to be more generic
    fun addMarker(marker: Marker)
    fun removeMarker(marker: Marker)
}