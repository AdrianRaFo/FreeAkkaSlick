package adrianrafo.routes

import akka.http.scaladsl.server.HttpApp
import akka.actor.ActorSystem
import freestyle.free.implicits._
import freestyle.free.http.akka._
import UserM._

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

}
