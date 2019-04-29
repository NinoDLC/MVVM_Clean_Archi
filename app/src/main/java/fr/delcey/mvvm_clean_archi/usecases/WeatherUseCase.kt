package fr.delcey.mvvm_clean_archi.usecases

import fr.delcey.mvvm_clean_archi.data.WeatherApiResponse

interface WeatherUseCase {
    fun getWeather(name: String?): WeatherApiResponse?
}
