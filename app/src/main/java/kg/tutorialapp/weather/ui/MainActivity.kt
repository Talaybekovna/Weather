package kg.tutorialapp.weather.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.weather.databinding.ActivityMainBinding
import kg.tutorialapp.weather.Extensions.format
import kg.tutorialapp.weather.models.Constants
import kg.tutorialapp.weather.models.ForeCast
import kg.tutorialapp.weather.network.WeatherClient
import kg.tutorialapp.weather.storage.ForeCastDatabase
import kg.tutorialapp.weather.ui.rv.DailyForeCastAdapter
import kg.tutorialapp.weather.ui.rv.HourlyForeCastAdapter
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dailyForeCastAdapter: DailyForeCastAdapter
    private lateinit var hourlyForeCastAdapter: HourlyForeCastAdapter

    private val db by lazy {
        ForeCastDatabase.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupRecyclerViews()
        getWeatherFromApi()
        subscribeToLiveData()

    }


    private fun setupViews() {
        binding.tvRefresh.setOnClickListener {
            showLoading()
            getWeatherFromApi()
        }
    }


    private fun setupRecyclerViews() {
        dailyForeCastAdapter = DailyForeCastAdapter()
        binding.rvDailyForecast.adapter = dailyForeCastAdapter

        hourlyForeCastAdapter = HourlyForeCastAdapter()
        binding.rvHourlyForecast.adapter = hourlyForeCastAdapter
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progress.visibility = View.GONE
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
                .subscribe({
                    hideLoading()
                },
                        {
                            hideLoading()
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        })
    }

    private fun subscribeToLiveData() {
        db.forecastDao().getAll().observe(this, Observer {
            it?.let {
                setValuesToViews(it)
                loadWeatherIcon(it)

                it.daily?.let {dailyList ->
                    dailyForeCastAdapter.setItemsDaily(dailyList)
                }

                it.hourly?.let { hourlyList ->
                    hourlyForeCastAdapter.setItemsHourly(hourlyList)
                }
            }
        })
    }

    private fun setValuesToViews(it: ForeCast) {
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
    }

    private fun loadWeatherIcon(it: ForeCast) {
        it.current?.weather?.get(0)?.icon?.let { icon ->
            Glide.with(this)
                    .load("${Constants.iconUri}${icon}${Constants.iconFormat}")
                    .into(binding.ivWeatherIcon)
        }
    }


}