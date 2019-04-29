package fr.delcey.mvvm_clean_archi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherApiResponse (
    @SerializedName("main")
    @Expose
    val weatherValues: WeatherValues,
    @SerializedName("name")
    @Expose
    val cityName: String
)