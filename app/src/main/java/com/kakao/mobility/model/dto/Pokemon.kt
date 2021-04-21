package com.kakao.mobility.model.dto

data class Pokemon(
    val name: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val sprites: Sprites = Sprites()
)

data class Sprites(
    val front_default: String = ""
)