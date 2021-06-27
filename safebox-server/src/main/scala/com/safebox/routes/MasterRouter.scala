package com.safebox.routes

import akka.http.scaladsl.server.Directives._

object MasterRouter {

  val routes = (DownloadRouter.apply
    ++ HealthCheckRouter.apply
    ++ UploadRouter.apply
    ++ FileManagementRouter.apply).reduce((a,b) => a ~ b)

}
