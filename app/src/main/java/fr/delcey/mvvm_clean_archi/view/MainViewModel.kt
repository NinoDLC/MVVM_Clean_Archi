package fr.delcey.mvvm_clean_archi.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    // We expose a LiveData but manipulate a MutableLiveData internaly
    private val _propertiesLiveData = MutableLiveData<List<PropertyUiModel>>()
    val propertiesLiveData: LiveData<List<PropertyUiModel>> = _propertiesLiveData

    // We keep reference to previous job to cancel it if necessary
    private var currentJob: Job? = null

    init {
        refreshData()
    }

    private fun refreshData() {

        // Cancel a previous job if a new one is scheduled before the end of the first one
        currentJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }

        // Coroutine stuff, look this up !!
        currentJob = viewModelScope.launch(Dispatchers.IO) {
            doStuffAsynchronously()
        }
    }

    private suspend fun doStuffAsynchronously() {
        // TODO MAKE SOME SQL QUERY HERE
        val mockList: List<PropertyUiModel> = listOf(
            PropertyUiModel(1, "FLAT", "10880 Malibu Point"),
            PropertyUiModel(2, "MANSION", "10236 Charing Cross Rd, Los Angeles, CA 90024, USA")
        )

        // Switches to the Main thread to set LiveData synchronously once API query is done
        withContext(Dispatchers.Main) {
            _propertiesLiveData.value = mockList
        }
    }

    fun insertData() {
        // TODO INSERT DATA
    }
}