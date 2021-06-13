import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives.{complete, get, path, respondWithHeaders}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO

import java.io.File
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}

object Server extends App {

  val host = "127.0.0.1"
  val port = 5000
  implicit val system: ActorSystem = ActorSystem(name = "todoapi")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  import system.dispatcher

  val bindingFuture = Http().bindAndHandle(routes.routes, host, port)

  bindingFuture.onComplete {
    case Success(_) => println("Success!")
    case Failure(error) => println(s"Failed: ${error.getMessage}")
  }

  import scala.concurrent.duration._
  Await.result(bindingFuture, 3.seconds)
}