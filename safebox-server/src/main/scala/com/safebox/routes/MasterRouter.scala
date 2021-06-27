package com.safebox.routes

import akka.http.scaladsl.server.Directives._

/**
 * MasterRouter gets all the routes from different object and uses for starting the server
 */
object MasterRouter {

  val routes = (DownloadRouter.apply
    ++ HealthCheckRouter.apply
    ++ UploadRouter.apply
    ++ FileManagementRouter.apply).reduce((a,b) => a ~ b)

}
