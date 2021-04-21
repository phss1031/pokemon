package com.kakao.mobility.ui.map

import androidx.lifecycle.*
import com.kakao.mobility.R
import com.kakao.mobility.model.dto.PokemonLocation
import com.kakao.mobility.model.dto.PokemonName
import com.kakao.mobility.model.repository.PokemonRepository
import com.kakao.mobility.ui.ViewStatus
import kotlinx.coroutines.CoroutineExceptionHandler

class MapViewModel(id: Long = 0L, private val repository: PokemonRepository) : ViewModel() {
    private val _viewStatus = MutableLiveData<ViewStatus>(ViewStatus.Loading)
    val viewStatus: LiveData<ViewStatus> get() = _viewStatus

    private val loadExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _viewStatus.postValue(ViewStatus.ErrorFinish(R.string.failed_to_load_pokemon_data))
    }

    private val liveDataId = liveData {
        emit(id)
    }

    val pokemonName: LiveData<PokemonName?> = liveDataId.switchMap {
        liveData(loadExceptionHandler) { emit(repository.getPokemonName(it)) }
    }

    val pokemonLocations: LiveData<List<PokemonLocation>> = liveDataId.switchMap {
        liveData(loadExceptionHandler) {
            emit(repository.loadLocation(it))
        }
    }

    fun getName(): String {
        return pokemonName.value?.names.takeIf { it?.isNotEmpty() ?: false }?.let {
            return it[0]
        } ?: ""
    }
}