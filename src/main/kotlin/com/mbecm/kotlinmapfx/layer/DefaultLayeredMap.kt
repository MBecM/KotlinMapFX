package com.mbecm.kotlinmapfx.layer

import com.mbecm.kotlinmapfx.layer.controller.DefaultMapController
import com.mbecm.kotlinmapfx.layer.controller.MapController
import com.mbecm.kotlinmapfx.layer.tile.DefaultTiledMap
import com.mbecm.kotlinmapfx.layer.tile.TiledMap
import javafx.scene.Parent
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Region

/**
 * @author Mateusz Becker
 */
class DefaultLayeredMap(tiledMap: TiledMap = DefaultTiledMap(), val controller: MapController = DefaultMapController(tiledMap)) : Region(), LayeredMap {
    init {
        children.add(tiledMap.getView())

        addEventHandler(MouseEvent.MOUSE_PRESSED, controller::mousePressed)
        addEventHandler(MouseEvent.MOUSE_DRAGGED, controller::mouseDragged)
        addEventHandler(ScrollEvent.SCROLL, controller::scroll)
    }

    override fun getView(): Parent {
        return this
    }
}