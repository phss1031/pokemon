package com.kakao.mobility.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kakao.mobility.model.dto.PokemonName
import com.kakao.mobility.model.repository.PokemonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest

sealed class SearchViewStatus(val index: Int) {
    object SearchResult : SearchViewStatus(0)
    object Empty : SearchViewStatus(1)
    object Error : SearchViewStatus(2)
    class ErrorToast(val throwable : Throwable) : SearchViewStatus(-1)
}

const val DEBOUNCE_TIME = 300L

class MainViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _viewStatus: MutableLiveData<SearchViewStatus> =
            MutableLiveData(SearchViewStatus.SearchResult)

    val viewStatus: LiveData<SearchViewStatus> get() = _viewStatus

    @ExperimentalCoroutinesApi
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResult = queryChannel.asFlow()
            .debounce(DEBOUNCE_TIME)
            .mapLatest(::search)
            .asLiveData()

    private suspend fun search(query: String): List<PokemonName> = try {
        val result = repository.search(query)
        updateViewStatus(getResultViewStatus(result))
        result
    } catch (t: Throwable) {
        updateViewStatus(SearchViewStatus.Error)
        emptyList()
    }

    private fun getResultViewStatus(result: List<PokemonName>) = when (result.size) {
        in 1..Integer.MAX_VALUE -> SearchViewStatus.SearchResult
        else -> SearchViewStatus.Empty
    }

    private fun updateViewStatus(viewStatus: SearchViewStatus) = _viewStatus.postValue(viewStatus)
}