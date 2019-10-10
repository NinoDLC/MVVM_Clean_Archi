package fr.delcey.mvvm_clean_archi.view

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.delcey.mvvm_clean_archi.Mock
import fr.delcey.mvvm_clean_archi.data.Address
import fr.delcey.mvvm_clean_archi.data.AddressDao
import fr.delcey.mvvm_clean_archi.data.Property
import fr.delcey.mvvm_clean_archi.data.PropertyDao
import fr.delcey.mvvm_clean_archi.view.model.PropertyUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val propertyDao: PropertyDao,
    private val addressDao: AddressDao
) : ViewModel() {

    // Don't use a Mediator since we are querying the data ourselves synchronously...
    // It's simpler to wait for all the data to load to actually merge the stuff
    private val _uiPropertiesLiveData = MutableLiveData<List<PropertyUiModel>>()
    val uiPropertiesLiveData: LiveData<List<PropertyUiModel>> = _uiPropertiesLiveData

    fun insertData() {
        viewModelScope.launch(Dispatchers.IO) {
            val newAddressId = addressDao.insertAddress(Address(path = Mock.getAddress()))

            propertyDao.insertProperty(Property(type = Mock.getType(), addressId = newAddressId))

            // Here if we would want to refresh the displayed list, we would have to do a new query
            // and change the livedata value with the result (that should contains one more row)

            // refreshData()
        }
    }

    /* *********************
     *  COROUTINE EXAMPLE  *
     ********************* */
    // https://developer.android.com/training/data-storage/room/accessing-data#kotlin-coroutines
    //
    // THIS IS A COMPLETELY DIFFERENT APPROACH TO DO QUERIES WITH ROOM WITH LIVEDATA, DON'T USE BOTH !
    // We don't use LiveData to "wait" for data from the repository,
    // We use LiveData to expose the UI data at the UI layer (as an Observable pattern usage), nothing more.
    // We use Coroutines to do asynchronous work
    // 
    // The tradeoff of this is the queries would be sequential, meaning the results would take up to O*n time to
    // compute, instead of O time with LiveData (O being the longest "work time" for a query and n the number of queries done)
    // Also, we wouldn't be notified that the underling data has changed : we lose reactivity to Room data.
    // The advantage is we query the data only when we need it, this could be usefull for large set of data that don't
    // have to be reactive
    fun refreshData() {

        // Coroutine stuff, look this up !!
        viewModelScope.launch(Dispatchers.IO) {
            doStuffOffMainThread()
        }
    }

    // This method is invoked in a coroutine, meaning it can block its thread if necessary, we are not on the MainThread :)
    @VisibleForTesting
    suspend fun doStuffOffMainThread() {

        val properties = propertyDao.getPropertiesAsSuspend()
        val addresses = addressDao.getAddressesAsSuspend()

        // Switches to the Main thread to set LiveData synchronously once API query is done
        withContext(Dispatchers.Main) {
            _uiPropertiesLiveData.value =
                properties
                    .map { property ->
                        buildUiModel(property, addresses.find { it.id == property.addressId })
                    }
        }
    }

    // Builds the Model to be presented to the View from the Models inside the data layer
    private fun buildUiModel(property: Property, address: Address? = null) =
        PropertyUiModel(
            property.id,
            property.type,
            makeHumanReadableAddress(address)
        )

    // TODO TO UNIT TEST !!
    // TODO THIS METHOD SHOULD BE USEFUL... RIGHT NOW IT'S NO USE FOR USER ! MAKE IT RETURN A NICE ADDRESS FOR USER
    @VisibleForTesting
    fun makeHumanReadableAddress(address: Address?): String? = address?.path

}