package kg.tutorialapp.weather.storage

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kg.tutorialapp.weather.models.CurrentForeCast

class ModelsConverter {

    @TypeConverter
    fun fromCurrentForecastToJson(foreCast: CurrentForeCast?): String? =         // serialization
        Gson().toJson(foreCast)

    @TypeConverter
    fun fromJsonToCurrentForecast(json: String?): CurrentForeCast? =      // deserialization
        Gson().fromJson(json, object: TypeToken<CurrentForeCast>() {}.type)
}