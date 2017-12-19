package com.mbecm.kotlinmapfx.layer

import com.mbecm.kotlinmapfx.layer.tile.TiledMap
import javafx.scene.Parent
import javafx.scene.layout.Region

/**
 * @author Mateusz Becker
 */
class DefaultLayeredMap(tiledMap: TiledMap) : Region(), LayeredMap {

    init {
        children.add(tiledMap.getView())
    }

    override fun getView(): Parent {
        return this
    }
}