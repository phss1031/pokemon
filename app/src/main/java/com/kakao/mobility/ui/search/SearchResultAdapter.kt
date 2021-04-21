package com.kakao.mobility.ui.search

import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kakao.mobility.databinding.ItemSearchResultBinding
import com.kakao.mobility.model.dto.PokemonName
import com.kakao.mobility.ui.search.SearchResultAdapter.BindingViewHolder
import com.kakao.mobility.BR

interface OnPokemonItemEvent {
    fun onPokemonSelected(id: Long)
}

class SearchResultAdapter(private val eventListener: OnPokemonItemEvent? = null) : ListAdapter<PokemonName, BindingViewHolder>(PokemonName.diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindingViewHolder(
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        holder.binding.setVariable(BR.pokemonName, getItem(position))
        eventListener?.let {
            holder.binding.setVariable(BR.eventListener, it)
        }
        holder.binding.executePendingBindings()
    }

    class BindingViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}