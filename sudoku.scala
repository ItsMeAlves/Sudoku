import org.jscala._
import org.jscala.JArray

object main extends App {
    val js = javascript {
        val fields = document.getElementById("fields")
    }
    println(js.asString)
}