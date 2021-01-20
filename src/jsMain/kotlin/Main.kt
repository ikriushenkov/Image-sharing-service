import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import styled.*

private val scope = MainScope()

val Main = functionalComponent<UserProps> { props ->

    val (posts, setPosts) = useState(emptyList<Post>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            setPosts(getPosts())
        }
    }

    styledDiv {
        css {
            position = Position.absolute
            left = 0.px
            width = LinearDimension("70%")
        }
        styledDiv {
            attrs {
            }
            css {
                textAlign = TextAlign.center
            }
            +"Recent posts"
        }
        styledUl {
            css {
                display = Display.inlineFlex
                alignItems = Align.center
                flexWrap = FlexWrap.wrap
                listStyleType = ListStyleType.none
            }
            posts.forEach {
                styledLi {
                    key = it.id.toString()
                    post {
                        post = it
                        width = 400
                    }
                }
            }
        }
    }

    styledDiv {
        css {
            position = Position.fixed
            right = 0.px
            width = LinearDimension("20%")
            backgroundColor = Color.white
            padding(1.em)
            borderRadius = 1.em
            border = "solid"
        }
        sendForm {
            onSubmit = { image ->
                val newPost = Post(image, props.user)
                setPosts(listOf(newPost) + posts)
                scope.launch {
                    addPost(newPost)
                }
            }
        }
    }
}

fun RBuilder.startPage(handler : UserProps.() -> Unit) : ReactElement {
    return child(Main) {
        this.attrs(handler)
    }
}