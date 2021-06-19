package com.safebox.routes

import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.{RequestContext, RouteResult}

import scala.concurrent.Future

object HealthCheckRouter{

  def route: RequestContext => Future[RouteResult] = path("healthCheck") {  get {complete("Server is up")  }}

}