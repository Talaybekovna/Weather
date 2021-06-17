package kg.tutorialapp.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.weather.models.CurrentForeCast
import kg.tutorialapp.weather.models.ForeCast
import kg.tutorialapp.weather.models.Weather
import kg.tutorialapp.weather.network.WeatherClient
import kg.tutorialapp.weather.storage.ForeCastDatabase

class MainActivity : AppCompatActivity() {

    lateinit var tv_forecast_list: TextView
    lateinit var button: Button

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

//    private val liveData = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_forecast_list = findViewById(R.id.tv_forecast_list)
        button = findViewById(R.id.button)

        makeRxCall()

// setValue() on Main Thread.      value = ,     postValue() out of Main Thread
//        button.setOnClickListener {
//            liveData.value = "HELLO"
//        }


        db.forecastDao().getAll().observe(this, Observer {
            tv_forecast_list.text = it.toString()
        })

    }

    @SuppressLint("CheckResult")
    private fun makeRxCall() {


        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
                .map {
                    db.forecastDao().deleteAll()
                    db.forecastDao().insert(it)
                    it
                }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
    }

//    @SuppressLint("CheckResult")
//    private fun getFromDb() {
//        db.forecastDao()
//                .getAll()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        {
//                            tv_forecast_list.text = it.toString()
//                        },
//                        {
//
//                        }
//                )
//    }
}