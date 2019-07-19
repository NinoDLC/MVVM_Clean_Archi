package fr.delcey.mvvm_clean_archi.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.delcey.mvvm_clean_archi.view.ViewModelFactory.Companion

@Suppress("UNCHECKED_CAST")
/** A singleton always has a private constructor, use [Companion.INSTANCE] to access instance of this. */
class ViewModelFactory private constructor() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        // Dependency injection
        val INSTANCE = ViewModelFactory(

        )
    }
}