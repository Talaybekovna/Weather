package kg.tutorialapp.weather.storage

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kg.tutorialapp.weather.DailyForeCast
import kg.tutorialapp.weather.HourlyForeCast

class CollectionsConverter {

    // HourlyForecast
    fun fromHourlyForecastListToJson(list: List<HourlyForeCast>): String =
        Gson().toJson(list)

    fun fromJsonToHourlyForecastList(json: String): List<HourlyForeCast> =
        Gson().fromJson(json, object: TypeToken<List<HourlyForeCast>>() {}.type)

    // DailyForecast
    fun fromDailyForecastListToJson(list: List<DailyForeCast>): String =
        Gson().toJson(list)

    fun fromJsonToDailyForecastList(json: String): List<DailyForeCast> =
        Gson().fromJson(json, object: TypeToken<List<DailyForeCast>>() {}.type)
}