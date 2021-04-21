package com.kakao.mobility.di

import com.google.gson.GsonBuilder
import com.kakao.mobility.model.remote.Client
import com.kakao.mobility.model.repository.PokemonRepository
import com.kakao.mobility.model.repository.PokemonRepositoryImpl
import com.kakao.mobility.ui.detail.PokemonDetailViewModel
import com.kakao.mobility.ui.main.MainViewModel
import com.kakao.mobility.ui.map.MapViewModel
import com.kakao.mobility.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val utilModule = module {
    single { GsonBuilder().setPrettyPrinting().create() }
    single { Client(get()) }
    single { get<Client>().createPokeMonService() }
    single { get<Client>().createPokeMonSearchService() }
}

val repository = module {
    single<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }
}

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { (id: Long) -> PokemonDetailViewModel(id, get(), get()) }
    viewModel { (id: Long) -> MapViewModel(id, get()) }
}

val applicationModule = listOf(
    repository,
    utilModule,
    viewModelModule
)