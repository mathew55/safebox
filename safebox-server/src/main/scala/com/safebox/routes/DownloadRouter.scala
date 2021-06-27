package com.safebox.routes

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, listDirectoryContents, parameters, path, respondWithHeaders}
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import akka.stream.scaladsl.FileIO

import java.io.File
import scala.concurrent.Future

object DownloadRouter {

  def listfiles: RequestContext => Future[RouteResult] = get {
    path("list") {
      println("Request recieved")
      listDirectoryContents("src/main/resources/")
    }
  }

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