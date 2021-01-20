import kotlinx.css.*
import kotlinx.html.A
import react.*
import styled.*

val Menu = functionalComponent<RProps> {

    styledH1 {
        css {
            textAlign = TextAlign.left
            color = Color.darkRed
            fontStyle = FontStyle.italic
            margin(0.px)
            padding(0.px)
        }
        +"Instagram"
    }
    styledUl {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceAround
            listStyleType = ListStyleType.none
            alignItems = Align.center
            padding(0.px)
            margin(0.px)
        }
        menuItem("#/posts") {
            +"All"
        }
        menuItem("#/") {
            +"Home"
        }
    }
}

fun RBuilder.menuItem(to : String, handler : StyledDOMBuilder<A>.() -> Unit) : ReactElement  {
    return styledLi {
        css {
            display = Display.block
            borderRadius = 1.px
            borderStyle = BorderStyle.solid
            margin(2.px)
        }
        styledA(href = to) {
            run(handler)
        }
    }
}

fun RBuilder.menu() : ReactElement {
    return child(Menu)
}