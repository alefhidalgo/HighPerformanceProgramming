package com.gft.akka.actors;

import java.math.BigInteger;

import akka.actor.AbstractLoggingActor;
import akka.actor.PoisonPill;
import akka.japi.pf.ReceiveBuilder;

import com.gft.akka.common.Protocol.UserFact;

// TODO: Auto-generated Javadoc
/**
 * The Class FactorialCalculatorActor.
 */
public class FactorialCalculatorActor extends AbstractLoggingActor {

  /**
   * Instantiates a new factorial calculator actor.
   */
  public FactorialCalculatorActor() {

    this.receive(ReceiveBuilder.match(UserFact.class, userFact -> {

      this.log().debug("DEBUG-6---TRANSFER CODE TO BE CALCULATED: " + userFact.getNum());
      BigInteger number = factorial(userFact.getNum());
      this.log().debug("DEBUG-6---CALCULATED FACTORIAL: " + number);
      this.sender().tell(new UserFact(userFact.getUserId(), userFact.getNum(), number), this.self());
      this.self().tell(PoisonPill.getInstance(), this.self());
    }).build());

  }

  /**
   * Factorial.
   *
   * @param n the n
   * @return the big integer
   */
  private static BigInteger factorial(int n) {
    if (n == 1) {
      return BigInteger.ONE;
    }
    return factorial(n - 1).multiply(BigInteger.valueOf(n));
    //return BigInteger.ONE;
  }

}
