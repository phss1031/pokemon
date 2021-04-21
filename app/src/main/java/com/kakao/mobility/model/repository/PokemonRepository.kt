package com.kakao.mobility.model.repository

import com.kakao.mobility.model.dto.Pokemon
import com.kakao.mobility.model.dto.PokemonLocation
import com.kakao.mobility.model.dto.PokemonName
import com.kakao.mobility.model.remote.PokemonAidService
import com.kakao.mobility.model.remote.PokemonService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.util.*

interface PokemonRepository {
    suspend fun refreshNames(): List<PokemonName>
    suspend fun refreshLocations(): Map<Long, List<PokemonLocation>>
    suspend fun refreshAll()
    suspend fun search(query: String = ""): List<PokemonName>
    suspend fun loadLocation(id: Long = 0): List<PokemonLocation>
    suspend fun getPokemonName(id: Long = 0): PokemonName?
    suspend fun getPokemon(id: Long = 0): Pokemon?
}

class PokemonRepositoryImpl(
        private val remotePokemon: PokemonService,
        private val remote: PokemonAidService
) : PokemonRepository {
    private var pokemonNames: List<PokemonName> = emptyList()
    private var pokemonLocations: Map<Long, List<PokemonLocation>> = emptyMap()

    override suspend fun refreshNames(): List<PokemonName> =
            remote.fetchPokemonNames().pokemons?.takeIf { it.isNotEmpty() }?.also {
                pokemonNames = it
            } ?: emptyList()

    override suspend fun refreshLocations(): Map<Long, List<PokemonLocation>> =
            remote.fetchPokemonLocations().pokemons.takeIf { it.isNotEmpty() }
                    ?.groupBy { location -> location.id }?.also {
                        pokemonLocations = it
                    } ?: emptyMap()

    private suspend fun validateNames() = takeIf { pokemonNames.isEmpty() }?.let {
        refreshNames()
    } ?: pokemonNames

    private suspend fun validateLocation() = takeIf { pokemonLocations.isEmpty() }?.let {
        refreshLocations()
    } ?: pokemonLocations

    override suspend fun refreshAll(): Unit = coroutineScope {
        listOf(async { refreshNames() }, async { refreshLocations() }).awaitAll()
    }

    override suspend fun search(query: String): List<PokemonName> {
        val pokemonNames = validateNames()

        return pokemonNames.filter { names ->
            names.queryInName(query)
        }
    }

    private fun PokemonName.queryInName(query: String) = names.any {
        it.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()).trim())
    }

    override suspend fun loadLocation(id: Long): List<PokemonLocation> {
        val pokemonLocations = validateLocation()
        return pokemonLocations[id] ?: emptyList()
    }

    override suspend fun getPokemonName(id: Long): PokemonName? {
        return pokemonNames.firstOrNull { it.id == id }
    }

    override suspend fun getPokemon(id: Long) = remotePokemon.fetchPokemonDetail(id)
}

