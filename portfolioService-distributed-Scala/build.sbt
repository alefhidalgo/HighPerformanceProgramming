import NativePackagerKeys._
 
packageArchetype.akka_application
 
name := "portfolioService-micro"
 

scalaVersion := "2.11.5"


mainClass in Compile :=  Some("com.gft.microservice.HPPMicro")
 
libraryDependencies ++= {
  val akkaVersion       = "2.3.9"
  val sprayVersion      = "1.3.1"
  Seq(
    "com.typesafe.akka"       %%  "akka-actor"                     % akkaVersion,
    "com.typesafe.akka"       %%  "akka-slf4j"                     % akkaVersion,
    "com.typesafe.akka"       %%  "akka-remote"                    % akkaVersion,
    "com.typesafe.akka"       %%  "akka-kernel"                    % akkaVersion,   
    "com.typesafe.akka"       %% "akka-slf4j"                      % akkaVersion,
    "io.spray"                %% "spray-json"                      % "1.2.6",
    "ch.qos.logback"          %  "logback-classic"                 % "1.1.2"    
  )
}