package adrianrafo.routes

import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import cats.Id
import cats.implicits._
import adrianrafo.dto.Tables.UserdataRow
import adrianrafo.persistence.Modules.Data
import adrianrafo.persistence.Services
import freestyle.free._
import freestyle.free.implicits._
import freestyle.free.slick.implicits._
import freestyle.free.loggingJVM.implicits._
import adrianrafo.persistence.Persistence._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
object UserM {

  @free
  trait UserApp {
    def get(id: Int): FS[UserdataRow]
  }

  trait Implicits {

    implicit val handler: UserApp.Handler[Id] = new UserApp.Handler[Id] {

      def get(id: Int): UserdataRow = {
       val user: UserdataRow =  Await.result(Services.print[Data.Op](id).interpret[Future], Duration.Inf)
        Await.result(Services.delete[Data.Op](id).interpret[Future], Duration.Inf)
        user
      }
    }

    implicit val userMarshaller: ToEntityMarshaller[UserdataRow] =
      Marshaller.StringMarshaller.compose((user: UserdataRow) =>
        s"UserdataRow(${user.id}, ${user.email}, ${user.name}, ${user.age})")

  }
  object implicits extends Implicits
}
