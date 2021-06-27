package com.safebox.routes

import akka.http.scaladsl.server.Directives.{complete, get, listDirectoryContents, parameters, path}
import akka.http.scaladsl.server.{RequestContext, RouteResult}

import java.io.File
import scala.concurrent.Future

class FileManagementRouter {

  def listfiles: RequestContext => Future[RouteResult] = get {
    path("list") {
      println("Request recieved")
      listDirectoryContents("src/main/resources/")
    }
  }

  def deleteFile: RequestContext => Future[RouteResult] =
    parameters("fileName") { fileName =>
      get {
        path("delete") {
          val file = new File(s"src/main/resources/$fileName")
          if (file.exists()) {
            file.delete()
            complete("File Successfully Deleted")
          } else {
            complete("Requested file not found in server, Use /list to check available files")
          }
        }
      }
    }
}
object FileManagementRouter{
  def apply: List[RequestContext => Future[RouteResult]] ={
    val fileManagementRouter = new FileManagementRouter()
    List(fileManagementRouter.listfiles,fileManagementRouter.deleteFile)
  }
}