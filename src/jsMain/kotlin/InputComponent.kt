import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import styled.*

external interface InputProps : RProps {
    var onSubmit : (String) -> Unit
}

val InputComponent = functionalComponent<InputProps> { props ->

    val (image, setImage) = useState("")

    val submitHandler : (Event) -> Unit  = {
        it.preventDefault()
        props.onSubmit(image)
        setImage("")
    }

    val changeHandler : (Event) -> Unit = {
        val text = (it.target as HTMLInputElement).value
        setImage(text)
    }

    form {
        legend {
            +"Do you want to upload your picture? Do it!"
        }
        attrs.onSubmitFunction = submitHandler
        div {
            label {
                attrs.htmlFor = "inputUrl"
                +"Url to picture:"
            }
            inputForm(InputType.url) {
                attrs {
                    onChangeFunction = changeHandler
                    value = image
                    id = "inputUrl"
                    classes = setOf("form-control")
                    placeholder = "Enter a url"
                }
            }
        }
        styledButton {
            css {
                margin(5.px)
            }
            +"Upload"
            attrs {
                type = ButtonType.submit
                onClickFunction = submitHandler
                classes = setOf("btn btn-dark")
            }
        }
    }
}

fun RBuilder.sendForm(handler : InputProps.() -> Unit ) : ReactElement {
    return child(InputComponent) {
        this.attrs(handler)
    }
}

fun RBuilder.inputForm(type : InputType, handler : StyledDOMBuilder<INPUT>.() -> Unit) : ReactElement {
    return styledInput(type) {
        css {
           fontSize = 18.px
        }
        run(handler)
    }
}