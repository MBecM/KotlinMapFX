package com.mbecm.kotlinmapfx.main

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage

/**
 * @author Mateusz Becker
 */
class Main : Application() {
    override fun start(primaryStage: Stage?) {
        primaryStage?.apply {
            scene = Scene(StackPane(Button("Init button")),500.0, 500.0)
            show()
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args);
}