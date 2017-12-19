package com.mbecm.kotlinmapfx.layer.tile

import javafx.scene.Parent

/**
 * @author Mateusz Becker
 */
interface TiledMap {
    var zoom: Int

    fun getView(): Parent

    //TODO: just for debuging, will be removed in future
    fun loadTiles()
}