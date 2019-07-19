package fr.delcey.mvvm_clean_archi.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.delcey.mvvm_clean_archi.R

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory.INSTANCE).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val mainAdapter = MainAdapter()

        val recyclerView : RecyclerView = findViewById(R.id.main_rv)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }

        // Update UI when ViewModel has a fresh data to display
        viewModel.propertiesLiveData.observe(this, Observer {
            mainAdapter.setData(it)
        })

        findViewById<Button>(R.id.main_btn_insert).setOnClickListener {
            viewModel.insertData()
        }
    }
}
