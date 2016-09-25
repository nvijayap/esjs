package spark.jobserver

import com.typesafe.config.{ConfigValueFactory, Config, ConfigFactory}
import spark.jobserver.io.{JobDAO, DataFileDAO}

import akka.actor.{Props, ActorSystem}
import spray.servlet.WebBoot

/**
 * This class is instantiated by the servlet initializer ; It needs to
 * have a default constructor and implement spray.servlet.WebBoot trait
 */
class Boot extends WebBoot {

  // We need an ActorSystem to host our application in
  implicit val system = ActorSystem("SJS")

  val config = ConfigFactory.load()
  val port = config.getInt("spark.jobserver.port")

  val clazz = Class.forName(config.getString("spark.jobserver.jobdao"))
  val ctor = clazz.getDeclaredConstructor(Class.forName("com.typesafe.config.Config"))
  val jobDAO = ctor.newInstance(config).asInstanceOf[JobDAO]
  val jarManager = system.actorOf(Props(classOf[JarManager], jobDAO), "jar-manager")
  val dataManager = system.actorOf(Props(classOf[DataManagerActor], new DataFileDAO(config)), "data-manager")
  val supervisor = system.actorOf(Props(classOf[LocalContextSupervisorActor], jobDAO), "context-supervisor")
  val jobInfo = system.actorOf(Props(classOf[JobInfoActor], jobDAO, supervisor), "job-info")

  // Create apiActor
  val apiActor = system.actorOf(Props(classOf[WebApiActor], system, config, port,
    jarManager, dataManager, supervisor, jobInfo), "webApi")

  // The service actor replies to incoming HttpRequests
  val serviceActor = apiActor
}
