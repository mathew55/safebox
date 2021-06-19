package com.safebox.routes

import akka.http.scaladsl.model.Uri.Path.Segment
import akka.http.scaladsl.server.Directives.{as, complete, entity, get, path}
import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}
import spray.json.enrichAny
import java.nio.file.{Path, Paths, Files}

import scala.concurrent.Future
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json.{DefaultJsonProtocol, RootJsonFormat, _}

import scala.concurrent.ExecutionContext.Implicits.global
object UserRouter {
  val userdirectory = "/Users/kuriakosemathew/Documents/work/safebox/safebox-server/user-data-store/"

  def userRoute: Route =
    (path("user" / "createruser" / IntNumber) & get) { id =>
      val folderPath: Path = Paths.get(s"$userdirectory${id}")
      if(!Files.exists(folderPath))
        Files.createDirectory(folderPath)
      complete(s"User created - ${id}")
    }~(path("user" / "deleteuser" / IntNumber) & get) { id =>
      complete(s"User deleted - ${id}")
    }



}