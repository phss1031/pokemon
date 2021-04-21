package com.kakao.mobility.model.remote

import com.kakao.mobility.model.dto.PokemonLocations
import com.kakao.mobility.model.dto.PokemonNames
import retrofit2.http.GET

interface PokemonAidService {
    @GET("pokemon_name")
    suspend fun fetchPokemonNames(): PokemonNames

    @GET("pokemon_locations")
    suspend fun fetchPokemonLocations(): PokemonLocations
}