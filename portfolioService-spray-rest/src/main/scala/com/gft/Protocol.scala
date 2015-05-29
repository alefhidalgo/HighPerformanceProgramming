package com.gft

/**
 * @author AOHG
 */

  
  
  
  
  object Protocol {
  import spray.json._
  import DefaultJsonProtocol._
  /**
   * Partial list of data for a user
   *
   * @param id the identity
   * @param firstName the first name
   * @param lastName the last name
   * @param email the email address
   * @param address current postal address
   * @param clientType type of bank client
   * @param job the current job of the user
   * @param transferCodes default transfer codes to be calculated
   * @param groups the list with names of the assigned groups to this user
   */
  case class UserPartialDataMessage(
    id: String,
    firstName: String,
    lastName: String,
    email: String,
    address: String,
    clientType: String,
    job: String,
    transferCodes: List[Int],
    groups: List[String])
   
   //JSON  
  object UserPartialDataMessage extends DefaultJsonProtocol {
    implicit val format = jsonFormat9 (UserPartialDataMessage.apply)
  }
  
    case class UserCompleteDataMessage(userId: String,
    firstName: String,
    lastName: String,
    email: String,
    address: String,

    clientType: String,
    job: String,
    transferCodes: List[Int],
    groups: List[Group])

  case class Group(
    name: String,
    entities: List[String],
    transferCodes: List[Int],
    active: Boolean)
    
      object Group extends DefaultJsonProtocol {
    implicit val format = jsonFormat4(Group.apply)
  }

}
  
  
