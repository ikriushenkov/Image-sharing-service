import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer

import kotlinx.browser.window

val endpoint = window.location.origin

val jsonClient = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}

suspend fun getPosts() : List<Post> {
    return jsonClient.get(endpoint + Post.apiPath)
}

suspend fun getPost(id : Long) : Post? {
    return jsonClient.get(endpoint + Post.apiPath + "/${id}")
}

suspend fun addPost(post : Post) {
    jsonClient.post<Unit>(endpoint + Post.apiPath) {
        contentType(ContentType.Application.Json)
        body = post
    }
}

suspend fun likePost(id : Long) {
    jsonClient.post<Unit>(endpoint + Post.apiPath + "/${id}?like=true")
}

suspend fun getComments(id : Long) : List<Comment> {
    return jsonClient.get(endpoint + Comment.apiPath + "/${id}")
}

suspend fun addComment(comment : Comment) {
    jsonClient.post<Unit>(endpoint + Comment.apiPath) {
        contentType(ContentType.Application.Json)
        body = comment
    }
}

suspend fun isAuthorized() : Boolean {
    return jsonClient.get<String>("$endpoint/api/checkSession") == "true"
}

suspend fun authorize(user : User) {
    jsonClient.post<Unit>(endpoint + User.apiPath) {
        contentType(ContentType.Application.Json)
        body = user
    }
}

suspend fun getInfo() : User {
    return jsonClient.get(endpoint + User.apiPath + "/name")
}