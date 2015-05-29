package com.gft.microservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import akka.actor.ActorSystem;
import akka.actor.Props;

import com.gft.microservice.actors.Dispatcher;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * The Class HPPMicro.
 */

@SpringBootApplication
public class HPPMicro implements CommandLineRunner {


  @Override
  public void run(String... arg0) throws Exception {
    Config config = ConfigFactory.load("backend");
    ActorSystem system = ActorSystem.create("dictionaryActorSystem", config);
    system.actorOf(Props.create(Dispatcher.class), "dispatcher");
  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(HPPMicro.class, args);
  }
}
