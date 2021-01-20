import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import kotlinx.html.*
import java.util.*

fun HTML.index() {
    head {
        styleLink("https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css")
        title("Instagram")
    }
    body {
        div {
            id = "root"
        }
        script(src = "/static/output.js") {}
    }
}

fun main() {
    val port = 8080

    DataBase.init()

    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }

        install(Sessions) {
            cookie<Session>("LOGIN_SESSION")
        }

        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            static("/static") {
                resources()
            }

            route(Post.apiPath) {
                get {
                    val id = call.sessions.get<Session>()!!.id

                    val posts = DataBase.getAllPosts()

                    call.respond(posts.reversed().map {
                        val user = DataBase.getUserById(it.authorId)

                        val likes = DataBase.getLikesByPostId(it.id)

                        Post(it.imageSrc, user, likes.size,
                            likes.any { session -> session == id })
                    })
                }
                post {
                    val post = call.receive<Post>()

                    val id = call.sessions.get<Session>()!!.id

                    DataBase.addPost(PostServer(post.id, post.imageSrc, id))

                    call.respond(HttpStatusCode.OK)
                }
                route("/{id}") {
                    get {
                        val id = call.sessions.get<Session>()!!.id
                        val postId = call.parameters["id"]!!.toLong()
                        val post = DataBase.getPostById(postId)

                        val user = DataBase.getUserById(post.authorId)

                        val likes = DataBase.getLikesByPostId(post.id)

                        call.respond(
                            Post(post.imageSrc, user, likes.size,
                                likes.any { session -> session == id })
                        )
                    }
                    post {
                        try {
                            val id = call.sessions.get<Session>()!!.id
                            val idPost = call.parameters["id"]!!.toLong()
                            val like = call.parameters["like"]!!.toBoolean()
                            if (like) {
                                val post = DataBase.getPostById(idPost)

                                val likes = DataBase.getLikesByPostId(post.id)

                                if (likes.any { it == id }) {
                                    DataBase.removeLike(Like(id, idPost))
                                } else {
                                    DataBase.addLike(Like(id, idPost))
                                }
                            }
                        } catch (e: Exception) {
                            call.respondText("error")
                        }
                    }
                }
            }
            route(Comment.apiPath) {
                get("/{id}") {
                    val postId = call.parameters["id"]!!.toLong()

                    val comments = DataBase.getCommentsByPostId(postId)
                    call.respond(comments.map {
                        val user = DataBase.getUserById(it.authorId)

                        Comment(user, it.content, it.postId)
                    })
                }
                post {
                    val id = call.sessions.get<Session>()!!.id
                    val comment = call.receive<Comment>()

                    DataBase.addComment(CommentServer(comment.idPost, id, comment.content))

                    call.respond(HttpStatusCode.OK)
                }
            }

            route(User.apiPath) {
                get("/name") {
                    val session = call.sessions.get<Session>()!!.id

                    val loginSession = DataBase.getUserById(session)

                    call.respond(User(loginSession.name, loginSession.avatarUrl))
                }

                post {
                    val id = UUID.randomUUID().toString()
                    val user = call.receive<User>()
                    val newSession = LoginSession(id, user.name, user.avatarUrl)
                    call.sessions.set(Session(id))
                    DataBase.addSession(newSession)
                    call.respond(HttpStatusCode.OK)
                }
            }

            get("/api/checkSession") {
                val session = call.sessions.get<Session>()
                call.respondText((session != null).toString())
            }
        }

    }.start(wait = true)
}