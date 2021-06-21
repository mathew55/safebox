package com.safebox.routes

import akka.http.scaladsl.server.Directives._

object MasterRouter {
  val routes = DownloadRouter.download ~ HealthCheckRouter.route ~ UserRouter.userRoute ~ UploadRouter.uploadRoute ~ DownloadRouter.listfiles
}
