package com.safebox.routes

import akka.http.scaladsl.server.Directives._

object MasterRouter {
  val routes = DownloadRouter.route2 ~ HealthCheckRouter.route ~ UserRouter.userRoute ~ UploadRouter.uploadRoute
}
