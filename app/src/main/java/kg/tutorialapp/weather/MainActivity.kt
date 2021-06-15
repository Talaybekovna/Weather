package kg.tutorialapp.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kg.tutorialapp.weather.network.PostsApi
import kg.tutorialapp.weather.network.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    lateinit var textView2: TextView
    lateinit var btn_start: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        textView2 = findViewById(R.id.textView2)
        btn_start = findViewById(R.id.btn_start)

        setup()

        //fetchWeather()
        //fetchWeatherUsingQuery()
        //fetchPostById()
        //createPost()
        //createPostUsingFields()
        //createPostUsingFieldMap()
        //updatePost()
        //deletePost()
    }

    private fun setup() {
        btn_start.setOnClickListener {
//            doSomeWork()
            makeRxCall()
        }
    }

    @SuppressLint("CheckResult")
    private fun makeRxCall() {

        val textView: TextView = findViewById(R.id.textView)
        val textView2: TextView = findViewById(R.id.textView2)

        WeatherClient.weatherApi.fetchWeather()
            .subscribeOn(Schedulers.io())     // на io-потоке будет запрос на сервер
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                textView.text = it.current?.weather[0].description
                textView2.text = it.current?.temp?.toString()
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            })
    }

    // just, create, fromCallable(), fromIterable()
    // disposable, compositeDisposable, clear(), dispose()
    // map, flatMap, zip
    private fun doSomeWork() {

        // наблюдаемый, Publisher
        val observable = Observable.create<String> { emitter -> // emit=излучать, издавать
            Log.d(TAG, "${Thread.currentThread().name} starting emitting")
            Thread.sleep(3000)
            emitter.onNext("Hello")
            Thread.sleep(1000)
            emitter.onNext("Bishkek")
            emitter.onComplete()

        }

        // наблюдатель, Subscriber
        val observer = object: Observer<String>{

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: String) {
                Log.d(TAG, "${Thread.currentThread().name} OnNext() $t")
            }

            override fun onError(e: Throwable) {

            }

            override fun onComplete() {

            }
        }

        observable
                .subscribeOn(Schedulers.computation())   // спец-й поток д/тяжелых вычислений / для observable
                .map {
                    Log.d(TAG, "${Thread.currentThread().name} starting mapping")
                    it.toUpperCase()        // mapping=преобразование, изменение, отображение
                }
                .observeOn(AndroidSchedulers.mainThread())   // observe б/обрабатывать в главном потоке
                .subscribe(observer)    // наблюдатель подписался на наблюдаемого
    }

    companion object{
        const val TAG = "RX"
    }

    /*---------call retrofit-------*/

    private fun deletePost() {
        val call = PostClient.postsApi.deletePost("42")

        call.enqueue(object: Callback<Unit>{
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                textView.text = response.code().toString()
            }

        } )
    }

    private fun updatePost() {
        val newPost = Post(userId = "20", body = "this is body") //title = "this is title",

        val call = PostClient.postsApi.patchPost(id = "42", post = newPost)

        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val resultPost = response.body()

                resultPost?.let {
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

        } )
    }

    private fun createPostUsingFieldMap() {
        val map = HashMap<String, String>().apply {
            put("userId", "55")
            put("title", "SUP")
            put("body", "Chuy")
        }

        val call = PostClient.postsApi.createPostUsingFieldMap(map)

        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val resultPost = response.body()

                resultPost?.let {
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

        } )
    }

    private fun createPostUsingFields() {

        val call = PostClient.postsApi.createPostUsingFields(userId = 99, title = "Hi!", body = "KARAKOL")

        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val resultPost = response.body()

                resultPost?.let {
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

        } )
    }

    private fun createPost() {
        // backend will generate & save new id, if we know id we should use @Put or @Patch
        val post = Post(userId = "42", title = "Hello", body = "BISHKEK")

        val call = PostClient.postsApi.createPost(post)

        call.enqueue(object: Callback<Post>{
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                val resultPost = response.body()

                resultPost?.let {
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

        } )
    }

    private fun fetchPostById() {
        val call = PostClient.postsApi.fetchPostById(10)

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
        val call = WeatherClient.weatherApi.fetchWeatherUsingQuery(lat = 40.513996, lon = 72.816101)

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

/*    private fun fetchWeather() {
        val call = WeatherClient.weatherApi.fetchWeather()

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
*/

}