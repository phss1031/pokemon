package com.kakao.mobility.model.dto

import androidx.recyclerview.widget.DiffUtil

data class PokemonName(
    val id: Long,
    val names: List<String>
) {
    companion object {
        var diffCallback: DiffUtil.ItemCallback<PokemonName> =
            object : DiffUtil.ItemCallback<PokemonName>() {
                override fun areItemsTheSame(oldItem: PokemonName, newItem: PokemonName) =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: PokemonName,
                    newItem: PokemonName
                ): Boolean = oldItem == newItem
            }
    }
}

data class PokemonNames(
    val pokemons: List<PokemonName>?
)