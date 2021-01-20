import kotlinx.css.*
import react.*
import styled.*

external interface CommentProps : RProps {
    var comment : Comment
}

val comment = functionalComponent<CommentProps> { props ->
    val curComment = props.comment

    styledDiv {
        css {
            border = "1px solid black"
            margin(5.px)
            display = Display.flex
            flexDirection = FlexDirection.column
            width = 650.px
            backgroundColor = Color.white
            borderRadius = 10.px
        }
        headerUser(curComment.author)
        styledDiv {
            css {
                borderTop = "1px solid"
                fontSize = 0.75.em
            }
            +curComment.content
        }
    }
}

fun RBuilder.comment(handler : CommentProps.() -> Unit) : ReactElement {
    return child(comment) {
        this.attrs(handler)
    }
}