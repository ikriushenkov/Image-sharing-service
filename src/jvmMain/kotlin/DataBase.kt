import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

data class LoginSession(val id : String, var name : String, var avatarUrl : String)

data class Session(val id : String)

data class PostServer(val id : Long, val imageSrc : String, val authorId : String)

data class Like(val userId : String, val postId : Long)

data class CommentServer(val postId : Long, val authorId: String, val content : String)

val linkLength = 500


object LoginSessions : Table() {
    val id = varchar("id", length = 50)

    val name = varchar("name", length = 50)
    val avatarUrl = varchar("avatarUrl", length = linkLength)

    override val primaryKey = PrimaryKey(id, name = "LS_ID")
}

object Likes : Table() {
    val userId = varchar("id", length = 50) references LoginSessions.id

    val postId = long("idPost") references Posts.id
}


object Posts : Table() {
    val id = long("id")

    val imageSrc = varchar("imageSrc", length = linkLength)

    val authorId = varchar("authorId", length = 50) references LoginSessions.id

    override val primaryKey = PrimaryKey(id, name = "Post_ID")
}

object Comments : Table() {
    val postId = long("idPost") references Posts.id

    val authorId = varchar("authorId", length = 50) references LoginSessions.id

    val content = varchar("content", length = 2000)
}

object DataBase {

    fun init() {
        Database.connect("jdbc:sqlite:instagram.db", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(LoginSessions, Likes, Posts, Comments)
        }
    }

    fun addSession(session : LoginSession) {
        transaction {
            LoginSessions.insert {
                it[id] = session.id
                it[name] = session.name
                it[avatarUrl] = session.avatarUrl
            }
        }
    }

    fun addLike(like : Like) {
        transaction {
            Likes.insert {
                it[userId] = like.userId
                it[postId] = like.postId
            }
        }
    }

    fun addPost(post : PostServer) {
        transaction {
            Posts.insert {
                it[id] = post.id
                it[imageSrc] = post.imageSrc
                it[authorId] = post.authorId
            }
        }
    }

    fun addComment(comment : CommentServer) {
        transaction {
            Comments.insert {
                it[postId] = comment.postId
                it[authorId] = comment.authorId
                it[content] = comment.content
            }
        }
    }

    fun getPostById(id : Long) : PostServer {
        val post = transaction {
            Posts.select { Posts.id eq id }.single()
        }
        return PostServer(post[Posts.id], post[Posts.imageSrc], post[Posts.authorId])
    }

    fun getCommentsByPostId(id : Long) : List<CommentServer> {
        return transaction {
            Comments.select { Comments.postId eq id }.map {
                CommentServer(it[Comments.postId], it[Comments.authorId], it[Comments.content])
            }
        }
    }

    fun getLikesByPostId(id : Long) : List<String> {
        return transaction {
            Likes.select { Likes.postId eq id }.map { it[Likes.userId] }
        }
    }

    fun getUserById(id : String) : User {
        val user = transaction {
            LoginSessions.select { LoginSessions.id.eq(id) }.single()
        }
        return User(user[LoginSessions.name], user[LoginSessions.avatarUrl])
    }

    fun getAllPosts() : List<PostServer> {
        return transaction {
            Posts.selectAll().map {
                PostServer(it[Posts.id], it[Posts.imageSrc], it[Posts.authorId])
            }
        }
    }

    fun removeLike(like : Like) {
        transaction {
            Likes.deleteWhere { Likes.postId.eq(like.postId) and Likes.userId.eq(like.userId) }
        }
    }
}