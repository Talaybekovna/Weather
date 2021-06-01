package kg.tutorialapp.weather

// vars from jsonplaceholder.typicode.com -> routes -> /posts/1

data class Post(
        var userId: String? = null,
        var id: String? = null,
        var title: String? = null,
        var body: String? = null
)