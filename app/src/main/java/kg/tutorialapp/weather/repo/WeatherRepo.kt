package kg.tutorialapp.weather.repo

import kg.tutorialapp.weather.network.WeatherApi
import kg.tutorialapp.weather.storage.ForeCastDatabase

class WeatherRepo(
        private val db: ForeCastDatabase,
        private val weatherApi: WeatherApi) {


}