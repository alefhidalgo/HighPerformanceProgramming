package controllers;

import static akka.pattern.Patterns.ask;
import play.Logger;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.fasterxml.jackson.databind.JsonNode;
import com.gft.akka.actors.PortfolioUserActor;
import com.gft.akka.common.Protocol.UserSetDataMessage;
import com.gft.akka.util.RemoteDictionaryCreator;
import com.google.gson.Gson;

public class Application extends Controller {

  private static ActorRef remote = null;

  /* Called by Global.java */
  public static void createRemote() {
    if (remote == null) {
      remote = new RemoteDictionaryCreator().createRemoteDictionary(Akka.system());
    }

  }

  public static F.Promise<Result> factorial() throws InterruptedException {
    Logger.info("FACTORIAL:" + request().body());
    JsonNode jsonData = request().body().asJson();
    Logger.info("JsonNode=" + jsonData);
    Logger.info("JSONDATA=" + jsonData.toString());
    UserSetDataMessage userSet = new Gson().fromJson(jsonData.toString(), UserSetDataMessage.class);

    return F.Promise.wrap(ask(Akka.system().actorOf(Props.create(PortfolioUserActor.class, remote)), userSet, 10000)).map(
      response -> ok(Json.toJson(response)));
  }

  public static Result index() {
    return ok(index.render("HPP PoC - Akka/Java8/MSA-Spring Boot"));
  }
}
