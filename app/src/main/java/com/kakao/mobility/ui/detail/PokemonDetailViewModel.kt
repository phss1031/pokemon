package com.kakao.mobility.ui.detail

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.kakao.mobility.R
import com.kakao.mobility.model.dto.Pokemon
import com.kakao.mobility.model.dto.PokemonLocation
import com.kakao.mobility.model.dto.PokemonName
import com.kakao.mobility.model.repository.PokemonRepository
import com.kakao.mobility.ui.ViewStatus
import kotlinx.coroutines.CoroutineExceptionHandler

class PokemonDetailViewModel(id: Long = 0L, private val repository: PokemonRepository, application: Application) :
        AndroidViewModel(application) {

    private val loadExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _viewStatus.postValue(ViewStatus.ErrorFinish(R.string.failed_to_load_pokemon_data))
    }

    private val _viewStatus = MutableLiveData<ViewStatus>(ViewStatus.Loading)
    val viewStatus: LiveData<ViewStatus> get() = _viewStatus

    private val liveDataId = liveData {
        id.takeIf { id > 0 }?.let { emit(id) }
                ?: _viewStatus.postValue(ViewStatus.ErrorFinish(R.string.invalidate_id))
    }

    val pokemonName: LiveData<PokemonName?> = liveDataId.switchMap {
        liveData(loadExceptionHandler) { emit(repository.getPokemonName(it)) }
    }

    val pokemon: LiveData<Pokemon?> = liveDataId.switchMap {
        liveData(loadExceptionHandler) {
            val pokemon = repository.getPokemon(it)
            _viewStatus.postValue(ViewStatus.Contents)
            emit(pokemon)
        }
    }

    val pokemonLocations: LiveData<List<PokemonLocation>> = liveDataId.switchMap {
        liveData(loadExceptionHandler) { emit(repository.loadLocation(it)) }
    }
}