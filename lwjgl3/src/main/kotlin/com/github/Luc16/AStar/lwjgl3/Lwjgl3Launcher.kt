package com.github.Luc16.AStar.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.github.Luc16.AStar.AStar
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.Luc16.AStar.HEIGHT
import com.github.Luc16.AStar.WIDTH


fun main() {
    Lwjgl3Application(AStar(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("AStar")
        setWindowedMode(WIDTH, HEIGHT)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
        setForegroundFPS(60)
    })
}
