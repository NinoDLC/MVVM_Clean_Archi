package fr.delcey.mvvm_clean_archi.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import fr.delcey.mvvm_clean_archi.R

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.INSTANCE).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Update UI when ViewModel has a fresh data to display
        viewModel.propertiesLiveData.observe(this, Observer {
            // TODO UPDATE VIEW
        })

        // TODO ADD A BUTTON TO REFRESH THE LIST
    }
}
