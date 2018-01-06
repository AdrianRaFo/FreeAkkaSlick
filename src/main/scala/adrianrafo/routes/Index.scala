/**
 * https://github.com/AdrianRaFo
 */
package adrianrafo.routes

import cats.implicits._
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

object Index extends App {
  Await.result(Services.create[Data.Op].interpret[Future], Duration.Inf)
  FreeServer.startServer("localhost", 16753)
}
