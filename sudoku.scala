import org.jscala._
import org.jscala.JArray

object main extends App {
    val js = javascript {
    	def getStartingArray():JArray[JArray[Int]] = {
	    	val array:JArray[JArray[Int]] = JArray()
	    	var column:Int = 1
	    	var row:Int = 1

	    	for(row <- 1 to 9) {
	    		val aux:JArray[Int] = JArray();
		    	for(column <- 1 to 9) {
		    		try {
		    			val element:String = (document.getElementById
		    			("l" + row + "c" + column).textContent)
		    			aux.push(parseInt(element))
		    		} catch {
		    			case ex: NumberFormatException => {
		    				aux.push(-1)
		    			}
		    		}
		    	}
		    	array.push(aux)
	    	}
	    	return array
    	}
    }
    println(js.asString)
}