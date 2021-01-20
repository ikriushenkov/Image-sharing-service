import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import styled.*


private val scope = MainScope()



val AllPosts = functionalComponent<RProps> {

    val (posts, setPosts) = useState(emptyList<Post>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            setPosts(getPosts())
        }
    }


    styledDiv {
        css {
            textAlign = TextAlign.center
        }
        +"Posts by all time:"
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

fun RBuilder.allPosts() : ReactElement {
    return child(AllPosts)
}