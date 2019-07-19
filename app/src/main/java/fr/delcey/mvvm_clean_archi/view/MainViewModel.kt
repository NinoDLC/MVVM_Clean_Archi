package fr.delcey.mvvm_clean_archi.view

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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

class MainViewModel(
    private val propertyDao: PropertyDao,
    private val addressDao: AddressDao
) : ViewModel() {

    // We expose a LiveData but manipulate a MediatorLiveData internaly
    private val _uiPropertiesLiveData = MediatorLiveData<List<PropertyUiModel>>()
    val uiPropertiesLiveData: LiveData<List<PropertyUiModel>> = _uiPropertiesLiveData

    init {
        wireUpMediator()
    }

    fun insertData() {
        viewModelScope.launch(Dispatchers.IO) {
            val newAddressId = addressDao.insertAddress(Address(path = Mock.getAddress()))

            propertyDao.insertProperty(Property(type = Mock.getType(), addressId = newAddressId))
        }
    }

    private fun wireUpMediator() {
        val propertiesLiveData = propertyDao.getProperties()
        val addressesLiveData = addressDao.getAddresses()

        _uiPropertiesLiveData.addSource(propertiesLiveData) {
            _uiPropertiesLiveData.value = combinePropertiesAndAddresses(
                it,
                addressesLiveData.value
            )
        }

        _uiPropertiesLiveData.addSource(addressesLiveData) {
            _uiPropertiesLiveData.value = combinePropertiesAndAddresses(
                propertiesLiveData.value,
                it
            )
        }
    }

    // TODO TO UNIT TEST !!
    @VisibleForTesting
    fun combinePropertiesAndAddresses(
        properties: List<Property>?,
        addresses: List<Address>?
    ): List<PropertyUiModel> {

        @Suppress("CascadeIf") // This is simpler this way... this is just for demo
        if (properties == null) {
            return listOf()
        } else if (addresses == null) {

            /* If the "map" line confuses you, consider this "map" line is the same as following :

            val listProperty = ArrayList<PropertyUiModel>()

            for (item in properties) {
                listProperty.add(PropertyUiModel(item.id, item.type))
            }

            return listProperty

            */

            return properties.map {
                buildUiModel(it)
            }
        } else {

            /* If the "zip/map" line confuses you, consider this "zip/map" line is the same as following :

            val listProperty = ArrayList<PropertyUiModel>()

            for (i in 0..min(properties.size, addresses.size)) {
                listProperty.add(
                    PropertyUiModel(
                        listProperty[i].id,
                        listProperty[i].type,
                        makeHumanReadableAddress(addresses[i])
                    )
                )
            }

            return listProperty

            */

            return properties
                .zip(addresses) // "Merge" 2 lists together and make a List<Pair<Property, Address>>
                .map {
                    buildUiModel(it.first, it.second)
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
    fun makeHumanReadableAddress(address: Address?): String? = address?.toString()
}