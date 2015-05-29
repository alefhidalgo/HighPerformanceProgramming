package com.gft.akka.actors;

import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Identify;
import akka.actor.ReceiveTimeout;
import akka.actor.Terminated;
import akka.japi.pf.ReceiveBuilder;

/**
 * The Class RemoteLookup.
 */
public class RemoteLookup extends AbstractLoggingActor {

  /** The path. */
  String path;

  /**
   * Instantiates a new remote lookup.
   *
   * @param path the path
   */
  public RemoteLookup(String path) {
    this.path = path;
    this.context().setReceiveTimeout(Duration.create("3 seconds"));
    this.sendIdentifyRequest();
    this.receive(this.identify());

  }

  /**
   * Identify.
   *
   * @return the partial function
   */
  final PartialFunction<Object, BoxedUnit> identify() {
    return ReceiveBuilder.match(ActorIdentity.class, id -> id.correlationId().equals(this.path) && id.getRef() != null, id -> {
      this.context().setReceiveTimeout(Duration.Undefined());
      this.log().info("switching to active state");
      ActorRef ref = id.getRef();
      this.context().become(this.active(ref));
      this.context().watch(ref);
    }).match(ActorIdentity.class, id -> id.getRef() == null, id -> {
      this.log().error("Remote actor with path " + this.path + " is not available.");
    }).match(ReceiveTimeout.class, r -> {
      this.sendIdentifyRequest();
    }).matchAny(x -> {
      this.log().error("Ignoring message " + x + ", remote actor is not ready yet.");
    }).build();

  }

  /**
   * Active.
   *
   * @param actor the actor
   * @return the partial function
   */
  final PartialFunction<Object, BoxedUnit> active(final ActorRef actor) {
    return ReceiveBuilder.match(Terminated.class, t -> t.actor().equals(actor), t -> {
      this.log().info("Actor " + (t.actor().path()) + " terminated.");
      this.context().become(this.identify());
      this.log().info("switching to identify state");
      this.context().setReceiveTimeout(Duration.create("3 seconds"));
      this.sendIdentifyRequest();
    }).matchAny(x -> {
      actor.forward(x, this.context());
    }).build();
  }

  /**
   * Send identify request.
   */
  private void sendIdentifyRequest() {
    this.log().info("---> accediendo al path remototo " + this.path);
    ActorSelection selection = this.context().actorSelection(this.path);
    selection.tell(new Identify(this.path), this.self());
  }

}