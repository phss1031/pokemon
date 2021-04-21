package com.kakao.mobility.model.remote

import com.kakao.mobility.model.dto.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {
    @GET("pokemon/{id}")
    suspend fun fetchPokemonDetail(@Path("id") id : Long) : Pokemon

}