package fr.delcey.mvvm_clean_archi.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherValues(
    @SerializedName("temp")
    @Expose
    val temperature: Double
)