
import java.util.concurrent.Executors

import org.http4s.client._
import cats.effect._
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec._
import scalafx.beans.property.{ObjectProperty, StringProperty}

import scala.concurrent.ExecutionContext.global

case class Quote(id: String, en: String, author: String, rating: Option[Double]){
  val id_ : StringProperty = new StringProperty(id)
  val en_ : StringProperty = new StringProperty(en)
  val author_ : StringProperty = new StringProperty(author)
  // don't make it a Double property, just won't compile for TableColumn[_,Double], don't ask me why
  val rating_  = new ObjectProperty(this, "rating", rating.getOrElse(0.0))
}

object HttpClient{

  val blockingPool = Executors.newFixedThreadPool(3)
  private val blocker = Blocker.liftExecutorService(blockingPool)
  private implicit val cs: ContextShift[IO] = IO.contextShift(global)

  private val httpClient: Client[IO] = JavaNetClientBuilder[IO](blocker).create

  def getQoutes: List[Quote] =
    httpClient.expect[List[Quote]]("https://programming-quotes-api.herokuapp.com/quotes/lang/en").unsafeRunSync()


}
