package com.safebox.routes

import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.{RequestContext, RouteResult}

import scala.concurrent.Future

class HealthCheckRouter{
  /**
   * api route which can be used to check the health of
   * application when deployed inside docker.
   * @return
   */
  def route: RequestContext => Future[RouteResult] = path("healthCheck") {  get {complete("Server is up")  }}

}
object HealthCheckRouter{
  def apply: List[RequestContext => Future[RouteResult]] ={
    val healthCheckRouterObject = new HealthCheckRouter()
    List(healthCheckRouterObject.route)
  }
}