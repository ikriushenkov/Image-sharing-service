import kotlinx.serialization.Serializable

@Serializable
data class User(val name : String, val avatarUrl : String) {
    companion object {
        const val apiPath = "/api/session"

        const val DefaultName = "Anonim"

        const val DefaultUrl = "https://www.w3schools.com/howto/img_avatar.png"

        val DefaultUser = User(DefaultName, DefaultUrl)
    }
}