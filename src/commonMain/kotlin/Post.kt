import kotlinx.serialization.Serializable

@Serializable
data class Post(val imageSrc: String,
                val author: User,
                var likes: Int = 0,
                var isLiked: Boolean = false) {
    val id = imageSrc.hashCode().toLong() - Int.MIN_VALUE

    companion object {
        const val apiPath = "/api/post"

        const val path = "post"
    }
}