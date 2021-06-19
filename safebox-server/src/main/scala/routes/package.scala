import akka.http.scaladsl.server.Directives._

package object routes {
  val routes = DownloadRouter.route2 ~ HealthCheckRouter.route ~ UserRouter.userRoute ~ UploadRouter.uploadRoute
}
