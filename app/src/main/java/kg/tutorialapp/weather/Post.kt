package kg.tutorialapp.weather

// vars from jsonplaceholder.typicode.com -> routes -> /posts/1

data class Post(
        var userId: Int? = null,
        var id: Int? = null,
        var title: String? = null,
        var body: String? = null
)