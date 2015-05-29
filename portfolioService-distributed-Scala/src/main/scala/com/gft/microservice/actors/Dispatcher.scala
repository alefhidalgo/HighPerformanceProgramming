package com.gft.microservice.actors

import akka.actor._
import scala.concurrent.duration._
import akka.util.Timeout
import com.gft.Protocol.UserPartialDataMessage
import com.gft.Protocol.Group
import com.gft.Protocol.UserCompleteDataMessage
import com.gft.microservice.actors.DictionaryMicroService.GetDictionary



class Dispatcher extends Actor  with ActorLogging {
  import context._
  implicit val timeout = Timeout(10 seconds)
  
 
  def receive = {
      
     case UserPartialDataMessage (userId, firstName, lastName,  email, address, clientType, job, transferCodes, groups) => {
      log.debug(s"(ACTOR#4) HELLO USER COMPLETE DATA RETRIEVER:  ${userId}")

      val name1: String = "Investor"
      val name2: String = "PrevilegedConsultor"
      val entities = List[String]("VISA", "4B")
      val transferCodes = List[Int](34, 56, 45)
      val active = true
      val group1: Group = new Group(name1, entities, transferCodes, active)
      val group2: Group = new Group(name2, entities, transferCodes, active)
      val completeGroups = List[Group](group1, group2)

      log.debug(s"(ACTOR#4) IT'S GOING TO SEND USER COMPLETE DATA:  ${userId}")
      
      
      val dic = UserCompleteDataMessage (userId, firstName, lastName, email, address , clientType, job, transferCodes, completeGroups)
      val dictionaryMicroService = context.actorOf(DictionaryMicroService.props(dic))//, userId)
      dictionaryMicroService.forward(GetDictionary) 
     }
 
  }

}
 