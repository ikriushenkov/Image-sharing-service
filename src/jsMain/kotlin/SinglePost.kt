import kotlinx.coroutines.*
import kotlinx.css.*
import react.dom.*
import react.*
import styled.*

external interface SinglePostProps : UserProps {
    var id : Long
}

private val scope = MainScope()

val singlePost = functionalComponent<SinglePostProps> { props ->

    val (curPost, setCurPost) = useState<Post?>(null)

    val (comments, setComments) = useState(emptyList<Comment>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            setCurPost(getPost(props.id))
            setComments(getComments(props.id))
        }
    }

    if (curPost != null) {
        styledDiv {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                alignItems = Align.center
            }
            post {
                post = curPost
                width = 550
            }
            comments.forEach {
                comment {
                    comment = it
                }
            }
            commentForm {
                onSubmit = { content ->
                    val newComment = Comment(props.user, content, props.id)
                    setComments(comments + listOf(newComment))
                    scope.launch {
                        addComment(newComment)
                    }
                }
            }
        }
    } else {
        div {
            +"There is no such post"
        }
    }
}

fun RBuilder.singlePost(handler : SinglePostProps.() -> Unit) : ReactElement {
    return child(singlePost) {
        this.attrs(handler)
    }
}