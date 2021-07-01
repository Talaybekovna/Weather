package kg.tutorialapp.weather.ui


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import kg.tutorialapp.weather.databinding.ActivityMainBinding
import kg.tutorialapp.weather.Extensions.format
import kg.tutorialapp.weather.models.Constants
import kg.tutorialapp.weather.models.ForeCast
import kg.tutorialapp.weather.ui.rv.DailyForeCastAdapter
import kg.tutorialapp.weather.ui.rv.HourlyForeCastAdapter
import org.koin.android.viewmodel.ext.android.getViewModel
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dailyForeCastAdapter: DailyForeCastAdapter
    private lateinit var hourlyForeCastAdapter: HourlyForeCastAdapter

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = getViewModel(MainViewModel::class)
        vm.getWeatherFromApi()
        setupViews()
        setupRecyclerViews()
        subscribeToLiveData()

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.i("TOKEN", it)
        }

        intent.getStringExtra("EXTRA")?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }

    }


    private fun setupViews() {
        binding.tvRefresh.setOnClickListener {
            vm.showLoading()
            vm.getWeatherFromApi()
        }
    }


    private fun setupRecyclerViews() {
        dailyForeCastAdapter = DailyForeCastAdapter()
        binding.rvDailyForecast.adapter = dailyForeCastAdapter

        hourlyForeCastAdapter = HourlyForeCastAdapter()
        binding.rvHourlyForecast.adapter = hourlyForeCastAdapter
    }


    private fun subscribeToLiveData() {
        vm.getForeCastAsLive().observe(this, Observer {
            it?.let {
                setValuesToViews(it)
                loadWeatherIcon(it)
                setDataToRecyclerViews(it)
            }
        })

        vm._isLoading.observe(this, Observer {
            when(it){
                true -> showLoading()
                false -> hideLoading()
            }
        })
    }

    private fun setDataToRecyclerViews(it: ForeCast) {
        it.daily?.let { dailyList ->
            dailyForeCastAdapter.setItemsDaily(dailyList)
        }

        it.hourly?.let { hourlyList ->
            hourlyForeCastAdapter.setItemsHourly(hourlyList)
        }
    }

    private fun showLoading() {
        binding.progress.post{
            binding.progress.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.progress.postDelayed({
            binding.progress.visibility = View.INVISIBLE
        }, 500)
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