import kotlinx.css.*
import styled.injectGlobal

fun applyStyle() {
    val styles = CSSBuilder().apply {
        body {
            backgroundColor = Color("#f2f3f4")
            margin(0.px)
            padding(0.px)
            fontFamily = "Georgia, sans-serif"
            fontSize = 25.px
        }
    }

    injectGlobal(styles.toString())
}
