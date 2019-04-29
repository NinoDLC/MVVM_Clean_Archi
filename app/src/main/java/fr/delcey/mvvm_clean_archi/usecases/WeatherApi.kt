package fr.delcey.mvvm_clean_archi.usecases

import fr.delcey.mvvm_clean_archi.data.WeatherApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    @GET("data/2.5/weather")
    fun getWeatherForCity(@Query("q") city: String?): Call<WeatherApiResponse>
}