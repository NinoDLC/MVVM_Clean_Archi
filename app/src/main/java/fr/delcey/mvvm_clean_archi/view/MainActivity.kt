package fr.delcey.mvvm_clean_archi.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import fr.delcey.mvvm_clean_archi.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.INSTANCE).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Update UI when ViewModel has a fresh data to display
        viewModel.weatherLiveData.observe(this, Observer {
            main_tv_result.text = it.cityTemperature
        })

        // Tell ViewModel the query has changed when user is typing
        findViewById<TextInputEditText>(R.id.main_et_input).addTextChangedListener {
            viewModel.getWeather(it?.toString())
        }
    }
}
