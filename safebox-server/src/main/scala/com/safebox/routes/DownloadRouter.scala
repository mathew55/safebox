package com.safebox.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path, respondWithHeaders}
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO

import java.io.File
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object DownloadRouter  {

  def route2: RequestContext => Future[RouteResult] = get {
    path("download") {
      val file = new File("/Users/kuriakosemathew/Documents/work/safebox/safebox-server/src/main/resources/download-test.txt")
      respondWithHeaders(RawHeader("Content-Disposition", s"""attachment; filename="download-test.txt"""")) {
        complete(HttpEntity(ContentTypes.`application/octet-stream`, FileIO.fromPath(file.toPath)))
      }
    }
  }

}