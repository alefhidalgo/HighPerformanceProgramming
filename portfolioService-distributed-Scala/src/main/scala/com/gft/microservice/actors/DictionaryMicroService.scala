package com.gft.microservice.actors
import akka.actor.{ PoisonPill, Actor, ActorLogging, Props }
import DictionaryMicroService._
import akka.actor.actorRef2Scala
import com.gft.Protocol.UserCompleteDataMessage


class DictionaryMicroService  (dictionary : UserCompleteDataMessage) extends Actor with ActorLogging {
  log.info(s"-->created dictionaryMicroService for user  ${dictionary.userId}")

  def receive = {

    case GetDictionary  =>
      sender ! dictionary
      self ! PoisonPill
      }    
}

object DictionaryMicroService {
  import spray.json._
  import DefaultJsonProtocol._
  
    
  def props(dictionary: UserCompleteDataMessage) = Props(classOf[DictionaryMicroService], dictionary)
  
  
  //messages
  case class DictionaryRequest(userId: String)
  
  case object GetDictionary
  case object NoUser
 

}
