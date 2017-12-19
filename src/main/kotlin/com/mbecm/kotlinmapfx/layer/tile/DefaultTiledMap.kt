package com.mbecm.kotlinmapfx.layer.tile

import javafx.scene.Group
import javafx.scene.Parent

/**
 * @author Mateusz Becker
 */
class DefaultTiledMap : Group(), TiledMap {
    override fun getView(): Parent {
        return this
    }

}