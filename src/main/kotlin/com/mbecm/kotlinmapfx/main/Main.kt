package com.mbecm.kotlinmapfx.main

import com.mbecm.kotlinmapfx.layer.DefaultLayeredMap
import com.mbecm.kotlinmapfx.layer.controller.DefaultMapController
import com.mbecm.kotlinmapfx.layer.tile.DefaultTiledMap
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

/**
 * @author Mateusz Becker
 */
class Main : Application() {

    val map = DefaultLayeredMap()

    override fun start(primaryStage: Stage?) {
//        map.con
        primaryStage?.apply {
            scene = Scene(map.getView(), 500.0, 500.0)
            show()
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args);
}