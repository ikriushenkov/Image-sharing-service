import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.dom.*
import react.*
import styled.*

external interface LoginProps : RProps {
    var onSubmit : (String, String) -> Unit
}

val login = functionalComponent<LoginProps> { props ->

    val (name, setName) = useState("")

    val (url, setUrl) = useState("")

    val submitHandler : (Event) -> Unit  = {
        it.preventDefault()
        props.onSubmit(if (name != "") name else User.DefaultName,
            if (url != "") url else User.DefaultUrl)
        setName("")
        setUrl("")
    }

    val changeHandler : (RSetState<String>) -> ((Event) -> Unit) = { setText ->
        {
            val text = (it.target as HTMLInputElement).value
            setText(text)
        }
    }

    styledForm {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
        }
        attrs.onSubmitFunction = submitHandler
        div {
            attrs.classes = setOf("form-group")
            label {
                attrs.htmlFor = "inputName"
                +"Name"
            }
            styledInput(InputType.text) {
                css {
                    width = 400.px
                }
                attrs {
                    onChangeFunction = changeHandler(setName)
                    value = name
                    classes = setOf("form-control")
                    id = "inputName"
                    placeholder = "Enter name"
                }
            }
            small {
                attrs.classes = setOf("form-text", "text-muted")
                +"Username does not have to be unique"
            }
        }
        div {
            label {
                attrs.htmlFor = "inputAvatar"
                +"Avatar url (optional):"
            }
            styledInput(InputType.url) {
                css {
                    width = 400.px
                }
                attrs {
                    onChangeFunction = changeHandler(setUrl)
                    value = url
                    classes = setOf("form-control")
                    id = "inputAvatar"
                    placeholder = "Enter avatar url (optional)"
                }
            }
        }
        styledButton {
            css {
                margin(5.px)
            }
            attrs {
                onClickFunction = submitHandler
                classes = setOf("btn btn-primary")
            }
            +"Login"
        }
    }
}

fun RBuilder.login(handler : LoginProps.() -> Unit) : ReactElement {
    return child(login) {
        this.attrs(handler)
    }
}