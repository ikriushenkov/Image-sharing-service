import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import styled.*

external interface PostProps : RProps {
    var post : Post
    var width : Int
}

private val scope = MainScope()

val post = functionalComponent<PostProps> { props ->
    val curPost = props.post

    val (count, setCount) = useState(curPost.likes)
    val (liked, setLiked) = useState(curPost.isLiked)

    val path = "#/${Post.path}/${curPost.id}"

    val absolutePath = "${window.location.origin}/${path}"

    val copyToClipboard : (Event) -> Unit = {
        val text = document.createElement("textArea") as HTMLTextAreaElement
        text.value = absolutePath
        document.body?.appendChild(text)
        text.select()
        document.execCommand("copy")
        document.body?.removeChild(text)
    }

    styledDiv {
        css {
            border = "1px solid black"
            margin(5.px)
            display = Display.flex
            flexDirection = FlexDirection.column
            backgroundColor = Color.white
            borderRadius = 12.px
        }
        headerUser(curPost.author)
        styledImg {
            css {
                border = "1px solid"
                margin((-1).px)
            }
            attrs {
                src = curPost.imageSrc
                width = props.width.toString()
            }
        }
        div {
            reactionButton {
                key = curPost.imageSrc
                css {
                    width = LinearDimension("40%")
                }
                +(if (liked) "❤️" else "\uD83E\uDD0D")
                +("$count")
                attrs {
                    classes = setOf("btn btn-outline-danger")
                    onClickFunction = {
                        if (liked) {
                            setCount(count - 1)
                            setLiked(false)
                        } else {
                            setCount(count + 1)
                            setLiked(true)
                        }
                        scope.launch {
                            likePost(curPost.id)
                        }
                    }
                }
            }
            a(href = path) {
                reactionButton {
                    css {
                        width = LinearDimension("35%")
                    }
                    +"\uD83D\uDCAC"
                    attrs.classes = setOf("btn btn-outline-secondary")
                }
            }

            reactionButton {
                css {
                    width = LinearDimension("25%")
                }
                +"Copy"
                attrs {
                    onClickFunction = copyToClipboard
                    classes = setOf("btn btn-outline-success")
                }
            }
        }
    }
}

fun RBuilder.post(handler : PostProps.() -> Unit) : ReactElement {
    return child(post) {
        this.attrs(handler)
    }
}

fun RBuilder.reactionButton(handler : StyledDOMBuilder<BUTTON>.() -> Unit) : ReactElement {
    return styledButton {
        css {
            borderRadius = 2.em
            borderStyle = BorderStyle.solid
            borderColor = Color.red
            display = Display.inlineBlock
            padding(0.px)
        }
        run(handler)
    }
}

fun RBuilder.headerUser(user : User) : ReactElement {
    return styledDiv {
        css {
            display = Display.flex
            alignItems = Align.center
        }
        img {
            attrs {
                src = user.avatarUrl
                width = "30"
                height = "30"
                alt = "Avatar"
                classes = setOf("rounded-circle")
            }
        }
        styledDiv {
            +user.name
        }
    }
}