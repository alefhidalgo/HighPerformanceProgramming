package com.gft.microservice.actors;

import java.util.Arrays;
import java.util.List;

import scala.concurrent.duration.Duration;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;

import com.gft.akka.common.Protocol.GetDictionary;
import com.gft.akka.common.Protocol.Group;
import com.gft.akka.common.Protocol.UserCompleteDataMessage;
import com.gft.akka.common.Protocol.UserPartialDataMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class Dispatcher.
 */
public class Dispatcher extends AbstractLoggingActor {

  /**
   * Instantiates a new dispatcher.
   */
  public Dispatcher() {
    // To set an initial delay
    this.getContext().setReceiveTimeout(Duration.create("10 seconds"));

    this.receive(ReceiveBuilder
      .match(
        UserPartialDataMessage.class,
        userPartialDataMessage -> {      
          this.log().debug("(ACTOR#4) HELLO USER COMPLETE DATA RETRIEVER: " + userPartialDataMessage.getId());

          String name1 = "Investor";
          String name2 = "PrevilegedConsultor";
          List<String> entities = Arrays.asList("VISA", "4B");
          List<Integer> transferCodes = Arrays.asList(34, 56, 45);
          boolean active = true;
          Group group1 = new Group(name1, entities, transferCodes, active);
          Group group2 = new Group(name2, entities, transferCodes, active);
          List<Group> completeGroups = Arrays.asList(group1, group2);

          this.log().debug("(ACTOciR#4) IT'S GOING TO SEND USER COMPLETE DATA: " + userPartialDataMessage.getId());

          UserCompleteDataMessage dic = new UserCompleteDataMessage(userPartialDataMessage.getId(), userPartialDataMessage.getFirstName(),
            userPartialDataMessage.getLastName(), userPartialDataMessage.getEmail(), userPartialDataMessage.getAddress(),
            userPartialDataMessage.getClientType(), userPartialDataMessage.getJob(), transferCodes, completeGroups);

          ActorRef dictionaryMicroService = this.getContext().actorOf(DictionaryMicroService.props(dic));
          // dictionaryMicroService.tell(new GetDictionary(), this.self());
          dictionaryMicroService.forward(new GetDictionary(), this.context());

        }).matchAny(x -> {
        this.log().info("Dispatcher " + this.self().path() + "UNKNOWN msg:" + x + ". from:" + this.sender().path());
      }).build());

  }
}
