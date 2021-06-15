package kg.tutorialapp.weather.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kg.tutorialapp.weather.ForeCast

@Database(
    entities = [ForeCast::class],
    version = 0,
    exportSchema = false
)

@TypeConverters(ModelsConverter::class, CollectionsConverter::class)
abstract class ForeCastDatabase: RoomDatabase() {
    abstract fun forecastDao(): ForeCastDao


    companion object{
        const val DB_NAME = "forecastDb"

        private var DB: ForeCastDatabase? = null
        fun getInstance(context: Context): ForeCastDatabase{
            if (DB == null){
                DB = Room.databaseBuilder(
                    context,
                    ForeCastDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()    // при обновлении version, всё разрушается и заново строится
                    .build()
            }
            return DB!!
        }
    }
}