package com.mbecm.kotlinmapfx.main

import com.mbecm.kotlinmapfx.coord.LatLon
import com.mbecm.kotlinmapfx.layer.DefaultLayeredMap
import com.mbecm.kotlinmapfx.layer.LayeredMap
import com.mbecm.kotlinmapfx.layer.controller.DefaultMapController
import com.mbecm.kotlinmapfx.layer.tile.DefaultTiledMap
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.input.MouseButton
import javafx.stage.Stage

/**
 * @author Mateusz Becker
 */
class Main : Application() {

    val map: LayeredMap = DefaultLayeredMap()

    override fun start(primaryStage: Stage?) {
        primaryStage?.apply {
            scene = Scene(map.getView(), 500.0, 500.0)
            show()
            map.setCoordinateConsumer(MouseButton.SECONDARY) { System.err.println("Latlon:  $it") }
            map.center(LatLon(54.5745,18.3908), 16)
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args);
}