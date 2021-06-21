package com.safebox.routes

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, listDirectoryContents, path, respondWithHeaders}
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import akka.stream.scaladsl.FileIO

import java.io.File
import scala.concurrent.{Future}

object DownloadRouter  {

  def listfiles: RequestContext => Future[RouteResult] = get {
    path("list") {
      println("Request recieved")
      listDirectoryContents("src/main/resources/")
    }
  }

  def download: RequestContext => Future[RouteResult] = get {
    (path("download") {
      val file = new File("src/main/resources/download-test.txt")
      respondWithHeaders(RawHeader("Content-Disposition", s"""attachment; filename="download-test.txt"""")) {
        complete(HttpEntity(ContentTypes.`application/octet-stream`, FileIO.fromPath(file.toPath)))
      }
    }
  }

}