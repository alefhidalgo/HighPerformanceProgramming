package com.gft.akka.util;

import play.Logger;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.gft.akka.actors.RemoteLookup;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class RemoteDictionaryCreator.
 */
public class RemoteDictionaryCreator {

  /** The protocol. */
  private final String protocol;

  /** The system name. */
  private final String systemName;

  /** The host. */
  private final String host;

  /** The port. */
  private final Integer port;

  /** The actor name. */
  private final String actorName;

  /**
   * Instantiates a new remote dictionary creator.
   */
  public RemoteDictionaryCreator() {
    Config config = ConfigFactory.load().getConfig("backend");
    // "akka.tcp://backend@0.0.0.0:2551/user/simple"
    this.protocol = config.getString("protocol");
    this.systemName = config.getString("system");
    this.host = config.getString("host");
    this.port = config.getInt("port");
    this.actorName = config.getString("actor");
  }

  /**
   * Creates the path.
   *
   * @return the string
   */
  private String createPath() {
    // $protocol://$systemName@$host:$port/$actorName"
    return String.format("%s://%s@%s:%s/%s", this.protocol, this.systemName, this.host, this.port, this.actorName);
  }

  /**
   * Creates the remote dictionary.
   *
   * @param context the context
   * @return the actor ref
   */
  public ActorRef createRemoteDictionary(ActorSystem context) {
    String path = this.createPath();
    Logger.info("$$$$$$$$$$$$$$$$$PATH:"+path);
    return context.actorOf(Props.create(RemoteLookup.class, path), "lookupDictionaryService");
  }

}
