package com.safebox.routes

import akka.http.scaladsl.server.Directives._

object MasterRouter {
  val routes = DownloadRouter.download ~ HealthCheckRouter.route ~  UploadRouter.uploadRoute ~ DownloadRouter.listfiles
}
