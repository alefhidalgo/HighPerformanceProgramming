package com.gft.akka.actors;

import java.util.ArrayList;
import java.util.List;

import scala.Option;
import scala.concurrent.duration.Duration;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorInitializationException;
import akka.actor.ActorKilledException;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;

import com.gft.akka.common.Protocol.Fact;
import com.gft.akka.common.Protocol.Facts;
import com.gft.akka.common.Protocol.UserSetDataMessage;




import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.stop;

// TODO: Auto-generated Javadoc
/**
 * The Class PortfolioUserActor.
 */
public class PortfolioUserActor extends AbstractLoggingActor {

  /** The nr users. */
  private Integer nrUsers = 0;

  /** The nr of results. */
  private Integer nrOfResults = 0;

  /** The captured sender. */
  private ActorRef capturedSender;

  /** The facts. */
  private List<Fact> facts = new ArrayList<Fact>();

  /**
   * Instantiates a new portfolio user actor.
   *
   * @param remoteDictionary the remote dictionary
   */
  PortfolioUserActor(ActorRef remoteDictionary) {
    this.context().setReceiveTimeout(Duration.create("10 seconds"));
    this.receive(ReceiveBuilder
      .match(UserSetDataMessage.class, userSetDataMessage -> {
        this.capturedSender = this.sender();
        this.nrUsers = userSetDataMessage.getUserSet().size();
        this.log().info("---> TAMA�O DE LA LISTA DE USUARIOS: " + this.nrUsers);
        userSetDataMessage.getUserSet().forEach(user -> {
          this.context().actorOf(Props.create(UserActor.class, remoteDictionary)).tell(user, this.self());
        });

      })
      .match(
        Fact.class,
        fact -> {
          this.nrOfResults += 1;
          this.log().info(
            "---> nrOfResults: " + this.nrOfResults + " / nrUsers(fijo): " + this.nrUsers
              + "  RECIBIDA LA LISTA DE FACTORIALES DEL USUARIO: " + (fact.getUserId()) + " ===> " + fact.getFact() + "");

          this.facts.add(fact);

          if (this.nrOfResults == this.nrUsers) {
            this.log().info("---> LOS FACTORIALES DE TODOS LOS USUARIOS (" + this.nrUsers + ") YA ESTAN COMPLETOS: " + this.facts);
            this.capturedSender.tell(new Facts(this.facts), this.self());
            this.self().tell(PoisonPill.getInstance(), this.self());
          }

        }).build());
  }

  @Override
  public void postRestart(Throwable reason) throws Exception {
    super.postRestart(reason);
    this.log().info("In master restart because of " + reason);
  }

  @Override
  public void preRestart(Throwable reason, Option<Object> message) throws Exception {
    // do not kill all children, which is the default here
    this.log().info("In master restart: " + message);
  }

  /*
   * SUPERVISION STRATEGY We do not have to specify our own supervisor strategy in each and every actor. This means that the
   * default supervisor strategy will take effect in every actor from this one.
   * http://doc.akka.io/docs/akka/snapshot/general/supervision.html
   */

  @Override
  public SupervisorStrategy supervisorStrategy() {
    return new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder.match(ActorInitializationException.class, e -> stop())
      .match(ActorKilledException.class, e -> stop()).match(ArithmeticException.class, e -> resume())
      .match(NullPointerException.class, e -> restart()).match(IllegalArgumentException.class, e -> stop()).matchAny(o -> escalate())
      .build());
  }

}
