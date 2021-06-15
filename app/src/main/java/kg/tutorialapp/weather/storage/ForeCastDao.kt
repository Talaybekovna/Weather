package kg.tutorialapp.weather.storage

import androidx.room.Dao
import androidx.room.Insert
import kg.tutorialapp.weather.ForeCast

@Dao
interface ForeCastDao {

    @Insert
    fun insert(foreCast: ForeCast)
}