package kg.tutorialapp.weather.network


import io.reactivex.Observable
import kg.tutorialapp.weather.models.ForeCast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//#2

interface WeatherApi {

    // openWeatherMap.org -> Example of API call

/*    @GET("onecall?lat=42.882004&lon=74.582748&exclude=minutely&appid=f4725e4a0fd0823ecd7f360cd4a9f45a&lang=ru&units=metric")
    fun fetchWeather(): Call<ForeCast>
    */

//    @GET("onecall")
//    fun  fetchWeatherUsingQuery(
//        @Query("lat") lat: Double = 42.882004,
//        @Query("lon") lon: Double = 74.582748,
//        @Query("exclude") exclude: String = "minutely",
//        @Query("appid") appid: String = "f4725e4a0fd0823ecd7f360cd4a9f45a",
//        @Query("lang") lang: String = "ru",
//        @Query("units") units: String = "metric"
//    ): Call<ForeCast>


    // retrofit rxjava
    @GET("onecall?lat=42.882004&lon=74.582748&exclude=minutely&appid=f4725e4a0fd0823ecd7f360cd4a9f45a&lang=ru&units=metric")
    fun fetchWeather(): Observable<ForeCast>


}