package fr.delcey.mvvm_clean_archi

import android.app.Application

class MainApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: MainApplication

        fun getApplication(): MainApplication = instance
    }
}
