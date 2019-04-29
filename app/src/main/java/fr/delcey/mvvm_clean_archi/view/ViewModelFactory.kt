package fr.delcey.mvvm_clean_archi.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.delcey.mvvm_clean_archi.usecases.WeatherUseCase
import fr.delcey.mvvm_clean_archi.usecases.WeatherUseCaseImpl
import fr.delcey.mvvm_clean_archi.view.ViewModelFactory.Companion

@Suppress("UNCHECKED_CAST")
/** A singleton always has a private constructor, use [Companion.INSTANCE] to access instance of this. */
class ViewModelFactory private constructor(private val weatherUseCase: WeatherUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(weatherUseCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        // Dependency injection
        val INSTANCE = ViewModelFactory(
            /** Could use any other class implementing [WeatherUseCase] interface here */
            WeatherUseCaseImpl()
        )
    }
}