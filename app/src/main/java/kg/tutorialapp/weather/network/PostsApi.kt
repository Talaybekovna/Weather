package kg.tutorialapp.weather.network

import kg.tutorialapp.weather.Post
import retrofit2.Call
import retrofit2.http.*

interface PostsApi {

    //jsonplaceholder.typicode.com -> Routes

//    @GET("posts/1")
//    fun fetchPostById(): Call<Post>

    @GET("posts/{id}")
    fun fetchPostById(
            @Path("id") id: Int // аннотация @Path, можем прописать пути
    ): Call<Post>

    @POST("posts")
    fun createPost(
            @Body post: Post
    ): Call<Post>

    @POST("posts")
    @FormUrlEncoded
    fun createPostUsingFields(
            @Field("userId") userId: Int,
            @Field("title") title: String,
            @Field("body") body: String
    ): Call<Post>

    @POST("posts")
    @FormUrlEncoded
    fun createPostUsingFieldMap(
            @FieldMap map: Map<String, String>
    ): Call<Post>
}