package com.kakao.mobility.model.dto

data class PokemonLocation(
    val id: Long = 0,
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

data class PokemonLocations(
    val pokemons: List<PokemonLocation> = emptyList()
)