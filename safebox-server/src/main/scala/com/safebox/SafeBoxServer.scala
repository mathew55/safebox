package com.safebox

import akka.http.scaladsl.Http
import com.safebox.routes.MasterRouter

import scala.concurrent.Await
import scala.util.{Failure, Success}

object SafeBoxServer extends App with AkkaSysProperties {

  import system.dispatcher
  val bindingFuture = Http().bindAndHandle(MasterRouter.routes, host, port)

  bindingFuture.onComplete {
    case Success(_) => system.log.info("Safebox Server Up and waiting for requests")
    case Failure(error) => system.log.info(s"Failed: ${error.getMessage}")
  }

  import scala.concurrent.duration._
  Await.result(bindingFuture, 3.seconds)
}
