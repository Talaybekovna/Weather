package kg.tutorialapp.weather.network


import io.reactivex.Observable
import io.reactivex.Single
import kg.tutorialapp.weather.models.ForeCast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//#2

interface WeatherApi {

    @GET("onecall")
    fun  fetchWeather(
        @Query("lat") lat: Double = 42.882004,
        @Query("lon") lon: Double = 74.582748,
        @Query("exclude") exclude: String = "minutely",
        @Query("appid") appid: String = "f4725e4a0fd0823ecd7f360cd4a9f45a",
        @Query("lang") lang: String = "ru",
        @Query("units") units: String = "metric"
    ): Single<ForeCast>


}