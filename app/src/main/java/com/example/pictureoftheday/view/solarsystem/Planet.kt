package com.example.pictureoftheday.view.solarsystem

data class Planet(
    val id: Int = 0,
    val type: Int = TYPE_EARTH,
    val name: String = "Earth",
    val description: String? = "Description Description Description Description Description Description Description Description Description Description Description Description ",
    val img: Int
) {
    companion object {
        const val TYPE_EARTH = 0
        const val TYPE_SUN = 1
        const val TYPE_PLANET = 2
    }
}