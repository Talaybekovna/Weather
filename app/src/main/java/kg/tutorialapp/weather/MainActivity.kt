package kg.tutorialapp.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kg.tutorialapp.weather.network.PostsApi
import kg.tutorialapp.weather.network.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    lateinit var textView2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)

        //fetchWeather()
        //fetchWeatherUsingQuery()
        fetchPostById()
    }

    private fun fetchPostById() {
        val call = postsApi.fetchPostById(10)

        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val post = response.body()

                // loop instead of let must use String Builder
                post?.let {
                    val resultText = "ID: " + it.id + "\n" +
                            "userID: " + it.userId + "\n" +
                            "TITLE: " + it.title + "\n" +
                            "BODY: " + it.body + "\n"

                    textView.text = resultText
                }
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    //#5
    private fun fetchWeatherUsingQuery() {
        val call = weatherApi.fetchWeatherUsingQuery(lat = 40.513996, lon = 72.816101)

        call.enqueue(object : Callback<ForeCast> {
            override fun onFailure(call: Call<ForeCast>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<ForeCast>, response: Response<ForeCast>) {
                if (response.isSuccessful) {
                    val foreCast = response.body()

                    foreCast?.let {
//                        val textView: TextView = findViewById(R.id.textView)
//                        val textView2: TextView = findViewById(R.id.textView2)
                        textView.text = it.current?.weather[0].description
                        textView2.text = it.current?.temp?.toString()

                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()

                    }
                }
            }

        })
    }

    private fun fetchWeather() {
        val call = weatherApi.fetchWeather()

        call.enqueue(object : Callback<ForeCast> {
            override fun onFailure(call: Call<ForeCast>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<ForeCast>, response: Response<ForeCast>) {
                if (response.isSuccessful) {
                    val foreCast = response.body()

                    foreCast?.let {
                        val textView: TextView = findViewById(R.id.textView)
                        val textView2: TextView = findViewById(R.id.textView2)
                        textView.text = it.current?.weather[0].description
                        textView2.text = it.current?.temp?.toString()

                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()

                    }
                }
            }

        })
    }

    private val okhttp by lazy {
        val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    //#3
    private val retrofit by lazy {
        Retrofit.Builder()
//                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttp)
                .build()
    }

    //#4
    private val weatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }

    private val postsApi by lazy {
        retrofit.create(PostsApi::class.java)
    }
}