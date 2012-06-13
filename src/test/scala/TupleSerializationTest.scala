import org.junit.Test
import org.junit.Assert
import org.junit.Assert._
import com.codahale.jerkson.Json

class TupleSerializationTest {
  @Test
  def serializedAsArray(): Unit = {
    val l = (1, "two", 3.0)
    assertEquals("[1,\"two\",3.0]", Json.generate(l))
  }
  
  @Test
  def parsedAsArray(): Unit = {
    val expected = (1, "two", 3.0)
    assertEquals(expected, Json.parse[(Int, String, Double)]("[1, \"two\", 3.0]"))
  }
}
