import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import styled.*

external interface CommentFormProps : RProps {
    var onSubmit : (String) -> Unit
}

val CommentForm = functionalComponent<CommentFormProps> { props ->

    val (comment, setComment) = useState("")

    val submitHandler : (Event) -> Unit  = {
        it.preventDefault()
        props.onSubmit(comment)
        setComment("")
    }

    val changeHandler : (Event) -> Unit = {
        val text = (it.target as HTMLTextAreaElement).value
        setComment(text)
    }

    styledForm {
        css {
            paddingTop = 25.px
        }
        attrs.onSubmitFunction = submitHandler
        div {
            attrs.classes = setOf("form-floating")
            styledTextArea {
                attrs {
                    onChangeFunction = changeHandler
                    value = comment
                    classes = setOf("form-control")
                    placeholder = "Leave a comment here"
                    id = "floatingTextarea"
                }
                css {
                    width = 550.px
                }
            }
            styledLabel {
                css {
                    fontSize = 15.px
                }
                attrs {
                   htmlFor = "floatingTextarea"
                }
                +"Leave a comment:"
            }
        }
        styledButton {
            css {
                margin(5.px)
            }
            +"Send"
            attrs {
                type = ButtonType.submit
                onClickFunction = submitHandler
                classes = setOf("btn btn-primary")
            }
        }
    }
}

fun RBuilder.commentForm(handler : CommentFormProps.() -> Unit) : ReactElement {
    return child(CommentForm) {
        this.attrs(handler)
    }
}