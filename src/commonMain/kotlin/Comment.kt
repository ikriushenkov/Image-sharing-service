import kotlinx.serialization.Serializable

@Serializable
data class Comment(val author : User, val content : String, val idPost : Long) {
    val id = content.hashCode().toLong() - Int.MIN_VALUE

    companion object {
        const val apiPath = "/api/comment"
    }
}