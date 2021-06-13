package routes

import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.{RequestContext, RouteResult}

import scala.concurrent.Future

object HealthCheckRouter extends App {

  def route: RequestContext => Future[RouteResult] = path("healthCheck") {  get {complete("Server is up")  }}

}