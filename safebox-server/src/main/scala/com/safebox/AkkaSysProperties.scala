package com.safebox

import akka.actor.ActorSystem
import akka.http.scaladsl.common.EntityStreamingSupport
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Flow
import akka.util.ByteString

trait AkkaSysProperties {
  val host = "127.0.0.1"
  val port = 5000
  implicit val system: ActorSystem = ActorSystem(name = "SafeBoxServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  import com.safebox.SafeBoxServer._
  val newline = ByteString("\n")

  implicit val jsonStreamingSupport = EntityStreamingSupport.json()
    .withFramingRenderer(Flow[ByteString].map(bs => bs ++ newline))
}
