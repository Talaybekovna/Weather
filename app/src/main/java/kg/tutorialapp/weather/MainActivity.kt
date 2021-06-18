package kg.tutorialapp.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.weather.databinding.ActivityMainBinding
import kg.tutorialapp.weather.network.WeatherClient
import kg.tutorialapp.weather.storage.ForeCastDatabase
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getWeatherFromApi()

        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {
        db.forecastDao().getAll().observe(this, Observer {
            it?.let {
                binding.tvTemperature.text = it.current?.temp?.roundToInt().toString()
                binding.tvTemperature.text = it.current?.temp?.roundToInt().toString()
                binding.tvDate.text = it.current?.date?.format()
                binding.tvTempMax.text = it.daily?.get(0)?.temp?.max?.roundToInt()?.toString()
                binding.tvTempMin.text = it.daily?.get(0)?.temp?.min?.roundToInt()?.toString()
                binding.tvFeelsLike.text = it.current?.feels_like?.roundToInt()?.toString()
                binding.tvWeather.text = it.current?.weather?.get(0)?.description
                binding.tvSunrise.text = it.current?.sunrise?.format("hh:mm")
                binding.tvSunset.text = it.current?.sunset?.format("hh:mm")
                binding.tvHumidity.text = "${it.current?.humidity?.toString()} %"

                it.current?.weather?.get(0)?.icon?.let { icon ->
                    Glide.with(this)
                            .load("https://openweathermap.org/img/wn/${icon}@2x.png")
                            .into(binding.ivWeatherIcon)
                }



            }
        })
    }

    @SuppressLint("CheckResult")
    private fun getWeatherFromApi() {

        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())
                .map {
                    db.forecastDao().insert(it)
                    it
                }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},
                    {
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