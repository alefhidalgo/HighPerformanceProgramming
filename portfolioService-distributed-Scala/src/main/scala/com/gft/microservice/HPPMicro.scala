package com.gft.microservice

import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
import com.gft.microservice.actors.Dispatcher
import akka.kernel.Bootable


class HPPMicro extends Bootable {

  val config = ConfigFactory.load("backend")
  val system = ActorSystem("dictionaryActorSystem", config)

 
  def startup = {
   system.actorOf(Props[Dispatcher], "dispatcher")
  }
 
  def shutdown = {
    system.shutdown()
  }

}
