
import java.util.concurrent.Executors
import org.http4s.client._
import cats.effect._
import io.circe.generic.auto._
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.CirceEntityCodec._
import scalafx.beans.property.{ObjectProperty, StringProperty}

import scala.concurrent.ExecutionContext.global

case class Quote(id: String, en: String, author: String, rating: Option[Double]) {
  val id_ : StringProperty = new StringProperty(id)
  val en_ : StringProperty = new StringProperty(en)
  val author_ : StringProperty = new StringProperty(author)
  // don't make it a Double property, just won't compile for TableColumn[_,Double], don't ask me why
  val rating_ = new ObjectProperty(this, "rating", rating.getOrElse(0.0))
}

object HttpClient {


  def getQuotesIO: IO[List[Quote]] =
    BlazeClientBuilder[IO].resource.use { client =>
      client.expect[List[Quote]]("https://programming-quotes-api.herokuapp.com/Quotes")
    }
}
