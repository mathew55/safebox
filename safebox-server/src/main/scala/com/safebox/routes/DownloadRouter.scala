package com.safebox.routes

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, parameters, path, respondWithHeaders}
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import akka.stream.scaladsl.FileIO

import java.io.File
import scala.concurrent.Future

class DownloadRouter {
  /**
   * api route which takes care of file downloads, takes in fileName to be
   * downloaded as parameter, Return file as stream if exists else displays
   * error message
   * @return
   */
  def download: RequestContext => Future[RouteResult] =
    parameters("fileName") { fileName =>
      get {
        path("download") {
          val file = new File(s"src/main/resources/$fileName")
          if (file.exists()) {
            respondWithHeaders(RawHeader("Content-Disposition", s"""attachment; filename="$fileName"""")) {
              complete(HttpEntity(ContentTypes.`application/octet-stream`, FileIO.fromPath(file.toPath)))
            }
          } else {
              complete("The Requested file is not available for download")
          }
        }
      }
    }
}

object DownloadRouter{
  def apply: List[RequestContext => Future[RouteResult]] ={
    val downloadRouterObject = new DownloadRouter()
    List(downloadRouterObject.download)
  }
}