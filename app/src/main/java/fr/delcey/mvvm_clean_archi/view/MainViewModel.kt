package fr.delcey.mvvm_clean_archi.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.delcey.mvvm_clean_archi.usecases.WeatherUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val useCase: WeatherUseCase) : ViewModel() {

    // We expose a LiveData but manipulate a MutableLiveData internaly
    private val _weatherLiveData = MutableLiveData<String>()
    val weatherLiveData: LiveData<String> = _weatherLiveData

    // We keep reference to previous job to cancel it if necessary
    private var currentJob: Job? = null

    fun getWeather(city: String?) {

        // Cancel a previous job if a new key has been pressed by user during the 500 ms waiting phase
        currentJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }

        // Coroutine stuff, look this up !!
        currentJob = viewModelScope.launch(Dispatchers.IO) {

            // Let user finish typing before actually querying the server (don't congest it)
            queryWeather(city)
        }
    }

    suspend fun queryWeather(city: String?) {
        delay(500)

        // Query server
        val weatherResponse = useCase.getWeather(city)

        // Switches to the Main thread to set LiveData synchronously once API query is done
        withContext(Dispatchers.Main) {
            if (weatherResponse != null) {
                _weatherLiveData.value =
                    "Dans la ville ${weatherResponse.cityName}, il fait ${weatherResponse.weatherValues.temperature}°C"
            } else {
                _weatherLiveData.value =
                    "$city est une ville inconnue au bataillon, entrez une vraie ville svp. " +
                            "Ou connectez-vous aux internets. "
            }
        }
    }
}