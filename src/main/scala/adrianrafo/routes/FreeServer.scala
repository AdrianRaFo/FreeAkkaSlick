package adrianrafo.routes

import akka.http.scaladsl.server.HttpApp
import akka.actor.ActorSystem
import akka.Done
import freestyle.free.implicits._
import freestyle.free.http.akka._
import UserM._

import scala.util.Try

object FreeServer extends HttpApp {

  import UserM.implicits._

  implicit val system: ActorSystem = ActorSystem("FreeAkkaSlick")

  val app = UserApp.to[UserApp.Op]

  override protected def routes = {
    (get & path("user" / IntNumber)) { id =>
      complete(app.get(id))
    }
  }

  override protected def postHttpBindingFailure(cause: Throwable): Unit =
    println(s"The server could not be started due to $cause")

  override protected def postServerShutdown(attempt: Try[Done], system: ActorSystem): Unit = {
    super.postServerShutdown(attempt, system)
    System.exit(0)
  }
}
