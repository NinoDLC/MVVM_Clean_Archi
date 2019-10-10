package fr.delcey.mvvm_clean_archi.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.delcey.mvvm_clean_archi.data.AddressDao
import fr.delcey.mvvm_clean_archi.data.AppDatabase
import fr.delcey.mvvm_clean_archi.data.PropertyDao
import fr.delcey.mvvm_clean_archi.view.ViewModelFactory.Companion

@Suppress("UNCHECKED_CAST")
/** A singleton always has a private constructor, use [Companion.INSTANCE] to access instance of this. */
class ViewModelFactory private constructor(
    private val propertyDao: PropertyDao,
    private val addressDao: AddressDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(propertyDao, addressDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        // Dependency injection
        val INSTANCE: ViewModelFactory

        init {
            val database = AppDatabase.getInstance()

            // Should use UseCases instead but this is shorter and clear approach
            INSTANCE = ViewModelFactory(
                database.propertyDao(),
                database.addressDao()
            )
        }
    }
}