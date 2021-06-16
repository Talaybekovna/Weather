package kg.tutorialapp.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.weather.models.CurrentForeCast
import kg.tutorialapp.weather.models.ForeCast
import kg.tutorialapp.weather.models.Weather
import kg.tutorialapp.weather.network.WeatherClient
import kg.tutorialapp.weather.storage.ForeCastDatabase

class MainActivity : AppCompatActivity() {

    lateinit var et_id: EditText
    lateinit var et_lat: EditText
    lateinit var et_long: EditText
    lateinit var et_description: EditText
    lateinit var btn_insert: Button
    lateinit var btn_update: Button
    lateinit var btn_delete: Button
    lateinit var btn_query: Button
    lateinit var btn_query_get_all: Button
    lateinit var tv_forecast_list: TextView

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_id = findViewById(R.id.et_id)
        et_lat = findViewById(R.id.et_lat)
        et_long = findViewById(R.id.et_long)
        et_description = findViewById(R.id.et_description)
        btn_insert = findViewById(R.id.btn_insert)
        btn_update = findViewById(R.id.btn_update)
        btn_delete = findViewById(R.id.btn_delete)
        btn_query = findViewById(R.id.btn_query)
        btn_query_get_all = findViewById(R.id.btn_query_all)
        tv_forecast_list = findViewById(R.id.tv_forecast_list)

        setup()

    }

    private fun getForecastFromInput(): ForeCast {
        val id = et_id.text?.toString().takeIf { !it.isNullOrEmpty() }?.toLong()
        val lat = et_lat.text?.toString().takeIf { !it.isNullOrEmpty() }?.toDouble()
        val long = et_long.text?.toString().takeIf { !it.isNullOrEmpty() }?.toDouble()
        val description = et_description?.text.toString()
        val current = CurrentForeCast(
            weather = listOf(Weather(description = description))
        )

        return ForeCast(
            id = id,
            lat = lat,
            lon = long,
            current = current
        )
    }

    private fun setup() {
        btn_insert.setOnClickListener {
            db.forecastDao()
                .insert(getForecastFromInput())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }

        btn_update.setOnClickListener {
            db.forecastDao()
                    .update(getForecastFromInput())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }

        btn_delete.setOnClickListener {
            db.forecastDao()
                    .delete(getForecastFromInput())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
        }

        btn_query_get_all.setOnClickListener {
            db.forecastDao()
                    .getAll()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                var text = ""

                                it.forEach {
                                    text += it.toString()
                                }

                                tv_forecast_list.text = text

                            },
                            {

                            }
                    )
        }

        btn_query.setOnClickListener {
            db.forecastDao()
                    .deleteAll()
//                    .getById(3L)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {

//                                tv_forecast_list.text = it.toString()

                            },
                            {

                            }
                    )
        }
    }

    @SuppressLint("CheckResult")
    private fun makeRxCall() {


        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())     // на io-потоке будет запрос на сервер
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({



            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
    }
}