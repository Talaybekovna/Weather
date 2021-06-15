package kg.tutorialapp.weather

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
//#1
// vars from openWeatherMap.org -> Api -> One Call Api -> Api Doc -> Example of API response
@Entity
data class ForeCast(
        @PrimaryKey(autoGenerate = true)
        val id: Long? = null,
        var lat: Double? = null,
        var lon: Double? = null,
        var timezone: String? = null,
        var timezone_offset: Long? = 0L,
        var current: CurrentForeCast,
        var hourly: List<HourlyForeCast>,
        var daily: List<DailyForeCast>
)

data class CurrentForeCast(
        @SerializedName("dt")
        var date: Long? = null,
        var sunrise: Long? = null,
        var sunset: Long? = null,
        var temp: Double? = null,
        var feels_like: Double? = null,
        var humidity: Int? = null,
        var weather: List<Weather>
)

data class Weather(
        var id: Long? = null,
        var description: String? = null,
        var icon: String? = null
)

data class HourlyForeCast(
        @SerializedName("dt")
        var date: Long? = null,
        var temp: Double? = null,
        var weather: List<Weather>,
        @SerializedName("pop")
        var probability: Double? = null
)

data class DailyForeCast(
        @SerializedName("dt")
        var date: Long? = null,
        var temp: Temperature? = null,
        var weather: List<Weather>? = null,
        @SerializedName("pop")
        var probability: Double? = null
)

data class Temperature(
        var min: Double? = null,
        var max: Double? = null
)
