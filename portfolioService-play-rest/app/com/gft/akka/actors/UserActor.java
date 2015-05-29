package com.gft.akka.actors;

import static akka.japi.Util.classTag;
import static akka.pattern.Patterns.ask;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.dispatch.OnComplete;
import akka.japi.pf.ReceiveBuilder;
import akka.util.Timeout;

import com.gft.akka.common.Protocol.Fact;
import com.gft.akka.common.Protocol.Group;
import com.gft.akka.common.Protocol.NumCodeFact;
import com.gft.akka.common.Protocol.UserBasicDataMessage;
import com.gft.akka.common.Protocol.UserCompleteDataMessage;
import com.gft.akka.common.Protocol.UserFact;
import com.gft.akka.common.Protocol.UserPartialDataMessage;

public class UserActor extends AbstractLoggingActor {

  final Timeout t = new Timeout(Duration.create(10, TimeUnit.SECONDS));
  final ExecutionContext ec = this.context().dispatcher();

  public Integer getNumOfFacts(UserCompleteDataMessage message) {
    Integer res = 0;
    for (Group group : message.getGroups()) {
      if (group.getActive()) {
        if (group.getTransferCodes().size() > 0) {
          res += group.getTransferCodes().size();
        } else {
          // If the group has not transfer codes use the default codes
          res += message.getTransferCodes().size();
        }
      }
    }
    return res;
  }

  /*
   * The list variable is initially set to an empty, or Nil list of BigInt numbers. This is where factorials results will be
   * stored as they are ready.
   */
  List<NumCodeFact> list = new ArrayList<NumCodeFact>();
  Integer nrOfFacts;
  Integer nrOfResults = 0;
  ActorRef capturedSender;

  public UserActor(ActorRef remoteDictionary) {

    this.receive(ReceiveBuilder
      /* case UserBasicDataMessage */
      .match(
        UserBasicDataMessage.class,
        userBasicDataMessage -> {
          // Assign the sender (ProtfolioUserActor instance) to a scope val
          this.capturedSender = this.sender();
          this.log().debug("-useractor- HELLO BASIC USER (ACTOR#2) " + userBasicDataMessage.getId());

          Future<UserPartialDataMessage> futurePartial = ask((this.context().actorOf(Props.create(UserDataActor.class))),
            userBasicDataMessage, this.t).mapTo(classTag(UserPartialDataMessage.class));
          /* UserPartialDataMessage from UserDataActor */
          futurePartial.onComplete(new OnComplete<UserPartialDataMessage>() {

            @Override
            public void onComplete(Throwable failure, UserPartialDataMessage resultPartial) throws Throwable {
              if (failure == null) {
                UserActor.this.log().debug("(ACTOR#2) RECEIVED UserPartialDataMessage:" + resultPartial.getId());

                Future<UserCompleteDataMessage> futureComplete = ask(remoteDictionary, resultPartial, UserActor.this.t).mapTo(
                  classTag(UserCompleteDataMessage.class));

                /* UserCompleteDataMessage from remoteDictionary */
                futureComplete.onComplete(new OnComplete<UserCompleteDataMessage>() {

                  @Override
                  public void onComplete(Throwable failure, UserCompleteDataMessage resultComplete) throws Throwable {
                    if (failure == null) {
                      UserActor.this.log().debug("(ACTOR#2) RECEIVED UserCompleteDataMessage: " + resultComplete.getId());
                      UserActor.this.log().debug("DEBUG-1---LIST OF GROUPS:" + resultComplete.getGroups().size());
                      UserActor.this.nrOfFacts = UserActor.this.getNumOfFacts(resultComplete);
                      UserActor.this.log().debug("------------_> numoffacts:" + UserActor.this.nrOfFacts);

                      for (Group group : resultComplete.getGroups()) {
                        UserActor.this.log().debug("DEBUG-2---IN LOOP");
                        if (group.getActive()) {
                          UserActor.this.log().debug("DEBUG-3---ACTIVE GROUP:" + group.getActive());
                          UserActor.this.log().debug("DEBUG-4---LIST OF TRASNFERCODES: " + group.getTransferCodes().size());
                          if (group.getTransferCodes().size() > 0) {
                            for (Integer transferCode : group.getTransferCodes()) {
                              UserActor.this.log().debug("DEBUG-5---TRANSFER CODE TO BE CALCULATED: " + transferCode);

                              UserActor.this.context().actorOf(Props.create(FactorialCalculatorActor.class))
                                .tell(new UserFact(resultComplete.getId(), transferCode, BigInteger.ZERO), UserActor.this.self());

                            }
                          } else {
                            // If the group has not transfer codes use the default codes
                            for (Integer transferCode : resultComplete.getTransferCodes()) {
                              // context.actorOf(Props[FactorialCalculatorActor]) ! UserFact (result.userId, transferCode, 0)

                              UserActor.this.context().actorOf(Props.create(FactorialCalculatorActor.class))
                                .tell(new UserFact(resultComplete.getId(), transferCode, BigInteger.ZERO), UserActor.this.self());
                            }
                          }
                        }
                      }

                    } else {
                      UserActor.this.log().error("error", failure);
                    }
                  }

                }, UserActor.this.ec);

              }
            }
          }, this.ec);

        })

      /* case UserFact */
      .match(
        UserFact.class,
        userFact -> {

          this.list.add(new NumCodeFact(userFact.getNum(), userFact.getFac()));
          this.nrOfResults += 1;
          this.log().info(
            "---> nrOfResults: " + this.nrOfResults + " / nrUsers(fijo): " + this.nrOfFacts + " (ACTOR#2) RECEIVED factorial for "
              + userFact.getUserId() + " - " + userFact.getNum() + ": " + userFact.getFac());

          if (this.nrOfFacts == this.nrOfResults) {
            this.log().info(" ---> RESUELTOS LOS " + this.nrOfFacts + " transfersCode para el user: " + userFact.getUserId());
            this.capturedSender.tell(new Fact(userFact.getUserId(), this.list), this.sender());
            this.self().tell(PoisonPill.getInstance(), this.self());
          }

        }).build());
  }
}
