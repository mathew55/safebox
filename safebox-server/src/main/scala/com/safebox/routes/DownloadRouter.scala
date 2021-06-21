package com.safebox.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, get, listDirectoryContents, path, respondWithHeaders}
import akka.http.scaladsl.server.{RequestContext, RouteResult}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO

import java.io.File
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import akka.http.scaladsl.model.Uri.Path.Segment
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, path}
import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}
import spray.json.enrichAny

import java.nio.file.{Files, Path, Paths}
import scala.concurrent.Future
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json.{DefaultJsonProtocol, RootJsonFormat, _}

import scala.concurrent.ExecutionContext.Implicits.global

object DownloadRouter  {

  def listfiles: RequestContext => Future[RouteResult] = get {
    path("list") {
      println("Request recieved")
      listDirectoryContents("src/main/resources/")
    }
  }

  def download: RequestContext => Future[RouteResult] = path("download" / Segment) { name =>
    println("Request Received")
    parameters("file"){ file2 =>
      println(s"src/main/resources/$file2")
      val file = new File(s"src/main/resources/$file2")
      if(file.exists()){
        respondWithHeaders(RawHeader("Content-Disposition",s"""attachment; filename="filename.jpg"""")) {
          complete(HttpEntity(ContentTypes.`application/octet-stream`, FileIO.fromPath(file.toPath)))
        }
      }else{
        complete(StatusCodes.InternalServerError)
      }
    }
  }

}