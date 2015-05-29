package com.gft.microservice.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

import com.gft.akka.common.Protocol.GetDictionary;
import com.gft.akka.common.Protocol.UserCompleteDataMessage;

// TODO: Auto-generated Javadoc
/**
 * The Class DictionaryMicroService.
 */
public class DictionaryMicroService extends AbstractLoggingActor {

  /**
   * Instantiates a new dictionary micro service.
   *
   * @param dictionary the dictionary
   */
  public DictionaryMicroService(UserCompleteDataMessage dictionary) {
    this.log().info("-->created dictionaryMicroService for user  " + dictionary.getId());

    this.receive(ReceiveBuilder.match(GetDictionary.class, x -> {

      this.sender().tell(dictionary, this.sender());
      this.self().tell(PoisonPill.getInstance(), this.self());

    }).matchAny(x -> {
      this.log().error("Unknown message: " + x);

    }).build());

  }

  /**
   * Props.
   *
   * @param dictionary the dictionary
   * @return the props
   */
  public static Props props(UserCompleteDataMessage dictionary) {

    return Props.create(DictionaryMicroService.class, dictionary);
  }

}
