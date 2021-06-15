package kg.tutorialapp.weather.storage

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kg.tutorialapp.weather.CurrentForeCast
import kg.tutorialapp.weather.ForeCast

class ModelsConverter {

    fun fromCurrentForecastToJson(foreCast: ForeCast): String =         // serialization
        Gson().toJson(foreCast)

    fun fromJsonToCurrentForecast(json: String): CurrentForeCast =      // deserialization
        Gson().fromJson(json, object: TypeToken<CurrentForeCast>() {}.type)
}