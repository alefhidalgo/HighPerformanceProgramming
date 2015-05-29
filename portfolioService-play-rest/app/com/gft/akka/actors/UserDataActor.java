package com.gft.akka.actors;

import java.util.Arrays;
import java.util.List;

import akka.actor.AbstractLoggingActor;
import akka.actor.PoisonPill;
import akka.japi.pf.ReceiveBuilder;

import com.gft.akka.common.Protocol.UserBasicDataMessage;
import com.gft.akka.common.Protocol.UserPartialDataMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class UserDataActor.
 */
public class UserDataActor extends AbstractLoggingActor {

  /**
   * Instantiates a new user data actor.
   */
  public UserDataActor() {
    this.receive(ReceiveBuilder.match(
      UserBasicDataMessage.class,
      userBasicDataMessage -> {
        this.log().debug("(ACTOR#3) HELLO USER PARTIAL DATA RETRIEVER:  " + userBasicDataMessage.getId());

        String address = "Calle Estrecha sn";
        String clientType = "VIP";
        String job = "Private Investigator";
        List<Integer> transferCodes = Arrays.asList(2, 3, 4);
        List<String> groups = Arrays.asList("Investor", "Conservative", "PrevilegedConsultor");

        this.log().debug("(ACTOR#3) IT'S GOING TO SEND USER PARTIAL DATA:  " + userBasicDataMessage.getId());

        UserPartialDataMessage userPartialDataMessage = new UserPartialDataMessage(userBasicDataMessage.getId(), userBasicDataMessage
          .getFirstName(), userBasicDataMessage.getLastName(), userBasicDataMessage.getEmail(), address, clientType, job, transferCodes,
          groups);
        this.sender().tell(userPartialDataMessage, this.self());
        this.self().tell(PoisonPill.getInstance(), this.self());
      }).build());
  }
}