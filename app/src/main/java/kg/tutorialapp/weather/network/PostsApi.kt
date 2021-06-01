package kg.tutorialapp.weather.network

import kg.tutorialapp.weather.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsApi {

    //jsonplaceholder.typicode.com -> Routes

//    @GET("posts/1")
//    fun fetchPostById(): Call<Post>

    @GET("posts/{id}")
    fun fetchPostById(
            @Path("id") id: Int // аннотация @Path, можем прописать пути
    ): Call<Post>
}