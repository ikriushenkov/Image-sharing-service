import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import react.dom.div
import react.router.dom.*
import styled.*

external interface IdProps : RProps {
    val id: Long
}

external interface UserProps : RProps {
    var user : User
}

private val scope = MainScope()

val Root = functionalComponent<RProps> {

    val (authorized, setAuthorized) = useState(false)

    val (curUser, setUser) = useState(User.DefaultUser)

    useEffect(dependencies = listOf()) {
        scope.launch {
            if (isAuthorized()) {
                setAuthorized(true)
                setUser(getInfo())
            } else {
                setAuthorized(false)
            }
        }
    }

    hashRouter {
        styledDiv {
            css {
                top = 0.px
                position = Position.fixed
                width = LinearDimension("100%")
                display = Display.flex
                justifyContent = JustifyContent.spaceBetween
                backgroundColor = Color.ghostWhite
                zIndex = 9999999
                border = "solid"
            }
            menu()
        }
        styledDiv {
            css {
                position = Position.relative
                top = 3.em
            }
            if (authorized) {
                switch {
                    route("/", exact = true) {
                        startPage {
                            user = curUser
                        }
                    }
                    route("/posts") {
                        allPosts()
                    }
                    route<IdProps>("/${Post.path}/:id") { props ->
                        val idPost = props.match.params.id
                        singlePost {
                            id = idPost
                            user = curUser
                        }
                    }
                    route("/404") {
                        div {
                            +"Sorry;("
                        }
                    }
                    redirect(from = "/", to = "/404")
                }
            } else {
                login {
                    onSubmit = { newName, newAvatarUrl ->
                        val newUser = User(newName, newAvatarUrl)
                        scope.launch {
                            authorize(newUser)
                            setAuthorized(true)
                            setUser(newUser)
                        }
                    }
                }
            }
        }
    }
}