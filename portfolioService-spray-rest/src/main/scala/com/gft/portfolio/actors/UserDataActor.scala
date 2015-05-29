package com.gft.portfolio.actors

import akka.actor.{ ActorLogging, Actor }
import com.gft._
import akka.actor.PoisonPill
import com.gft.Protocol.UserPartialDataMessage

class UserDataActor extends Actor with ActorLogging {
  import com.gft.portfolio.actors.PortfolioUserActor._  
  
  def receive = {
    case UserBasicDataMessage(id, firstName, lastName, email) => {
      log.debug(s"(ACTOR#3) HELLO USER PARTIAL DATA RETRIEVER:  ${id}")

      /*
        TEST PURPOSES ONLY!!!!!!!!!!!!!
          Read json stub
          create UserPartialDataMessage
          return to sender
       */
      val address: String = "Calle Estrecha sn"
      val clientType: String = "VIP"
      val job: String = "Private Investigator"
      val transferCodes = List[Int](2, 3, 4)
      val groups = List[String]("Investor", "Conservative", "PrevilegedConsultor")

      log.debug(s"(ACTOR#3) IT'S GOING TO SEND USER PARTIAL DATA:  ${id}")
      sender ! new UserPartialDataMessage(id, firstName, lastName, email, address, clientType, job, transferCodes, groups)
      //self ! PoisonPill
    }
  }
}

