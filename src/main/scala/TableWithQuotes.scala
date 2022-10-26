
import cats.effect.unsafe.implicits.global
import org.scalafx.extras.BusyWorker
import org.scalafx.extras.BusyWorker.SimpleTask
import scalafx.Includes._
import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.scene.{Cursor, Scene}
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{Button, Label, TableColumn, TableView}
import scalafx.scene.layout.{BorderPane, Priority, VBox}

object TableWithQuotes extends JFXApp {

  HttpClient // init

  val quotes = ObservableBuffer[Quote](
    Quote("id1", "blahblah", "me", Some(10.0)),
    Quote("id2", "more blahblah", "you", Some(1.0)),
    Quote("id3", "Click fetch quotes", "me", None)
  )

  val tableView=new TableView[Quote](quotes) {
    selectionModel.value.selectedItem.onChange {
      val quote=selectionModel.value.selectedItem()
      println(quote)
    }
    style = "-fx-font-size: 12pt"
    columns ++= List(
      new TableColumn[Quote, String] {
        text = "ID"
        cellValueFactory = {_.value.id_}
        prefWidth = 100
      },
      new TableColumn[Quote, String] {
        text = "Text"
        cellValueFactory = {_.value.en_}
        prefWidth = 500
      },
      new TableColumn[Quote, String] {
        text = "Author"
        cellValueFactory = {_.value.author_}
        prefWidth = 150
      } ,
      new TableColumn[Quote, Double] {
        text = "Rating"
        cellValueFactory = {_.value.rating_}
        prefWidth = 10
      }

    )
  }

  val fetchQuotes: Button =new Button{
    cursor=Cursor.Hand
    style = "-fx-font-size: 24pt"

    delegate.setMaxSize(Double.MaxValue,Double.MaxValue)

    text="Fetch Quotes"
    onAction= { _ =>
      new BusyWorker("Fetching via http4s", fetchQuotes){
        status.text <== progressMessage
      }.doTask(
        new SimpleTask[Unit] {
           def call(): Unit = {

            message() = "Fetching quotes"

            HttpClient.getQuotesIO.map { quoteList =>
                          println("adding to GUI")
                          quotes ++= quoteList
                        }.unsafeRunSync()
             message() =""
          }
        }
      )

      println("exiting click handler")
    }
  }

  val clearQuotes=new Button{
    cursor=Cursor.Hand
    style = "-fx-font-size: 24pt"

    delegate.setMaxSize(Double.MaxValue,Double.MaxValue)

    text="Clear Quotes"
    onAction={ _=>
      Platform.runLater {
        quotes.clear()
      }
    }
  }

  private val status = new Label("") {
    hgrow = Priority.Always
    maxWidth = Double.MaxValue
  }

   stage = new PrimaryStage {
    title = "TableWithQuotes"
    scene = new Scene {
      stylesheets=List("style.css")
      root = new BorderPane(tableView, fetchQuotes, null, new VBox{
        children ++= Seq(clearQuotes, status)
      }, null)
    }
  }

  override def stopApp(): Unit = {
    println("exiting")
//    HttpClient.blockingPool.shutdown()
  }
}