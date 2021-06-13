package kg.tutorialapp.weather

import kg.tutorialapp.weather.network.PostsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PostClient {

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

    val postsApi by lazy {
        retrofit.create(PostsApi::class.java)
    }

}