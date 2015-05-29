package com.gft.test.akka.actors;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gft.akka.actors.FactorialCalculatorActor;
import com.gft.akka.common.Protocol.UserFact;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import akka.testkit.JavaTestKit;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


public class FactorialCalculatorActorTest {
  
  static ActorSystem system;
  
  @BeforeClass
  public static void setup() {
    system = ActorSystem.create();
  }
  
  @AfterClass
  public static void teardown() {
    JavaTestKit.shutdownActorSystem(system);
    system = null;
  }
 
  /**
   * Test FactorialCalculatorActor actor 
   * 5 -> 120
   * @throws Exception
   */
  @Test 
  public void testFactorial() throws Exception {
    final Props props = Props.create(FactorialCalculatorActor.class);
    final TestActorRef<FactorialCalculatorActor> ref = TestActorRef.create(system, props, "testFactorial");
    
    UserFact fact = new UserFact("user-12345667", 5, BigInteger.ZERO);
    
    final Future<Object> future = akka.pattern.Patterns.ask(ref, fact, 3000);
    assertTrue(future.isCompleted());
    fact = (UserFact) Await.result(future, Duration.Zero());
    assertEquals(new BigInteger("120"), fact.getFac());
  }

}
