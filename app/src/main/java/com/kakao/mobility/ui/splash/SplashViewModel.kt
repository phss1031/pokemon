package com.kakao.mobility.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.mobility.model.repository.PokemonRepository
import kotlinx.coroutines.launch

sealed class SplashStatus {
    object Init : SplashStatus()
    object LoadCompleted : SplashStatus()
    class Error(val throwable: Throwable) : SplashStatus()
}

class SplashViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _status: MutableLiveData<SplashStatus> = MutableLiveData(SplashStatus.Init)
    val status: LiveData<SplashStatus>
        get() = _status

    init {
        loadPokemons()
    }

    private fun loadPokemons() = viewModelScope.launch {
        try {
            repository.refreshAll()
            _status.postValue(SplashStatus.LoadCompleted)
        } catch (t : Throwable){
            Log.d("Error", "Failed to load pokemon data - $t")
            _status.postValue(SplashStatus.Error(t))
        }
    }

    fun isCompleted() : Boolean = status.value == SplashStatus.LoadCompleted
}