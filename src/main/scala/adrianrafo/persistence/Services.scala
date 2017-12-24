/*
 * Copyright 2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package adrianrafo.persistence

import cats.implicits._

import freestyle.free._
import freestyle.free.slick._
import freestyle.free.slick.implicits._

import scala.concurrent.ExecutionContext
import adrianrafo.dto.Tables._
import Persistence._
import Modules._

object Services {

  def create[F[_]: SlickM](
      implicit data: Data[F],
      executionContext: ExecutionContext): FreeS[F, Unit] = {
    for {
      _  <- createSchema.liftFS[F]
      _  <- data.log.info("Created schema")
      id <- insertUser(UserdataRow(0, "a", "a@g.com", Some(12))).liftFS[F]
      _  <- data.log.info(s"Inserted user with id $id")
    } yield ()
  }

  def print[F[_]: SlickM](id: Int)(
      implicit data: Data[F],
      executionContext: ExecutionContext): FreeS[F, UserdataRow] = {
    for {
      user <- getUser(id).liftFS[F]
    } yield user
  }

  def delete[F[_]: SlickM](
      id: Int)(implicit data: Data[F], executionContext: ExecutionContext): FreeS[F, Unit] = {
    for {
      numDeletes <- deleteUser(id).liftFS[F]
      _          <- data.log.info(s"Deletes: $numDeletes")
      _          <- dropSchema.liftFS[F]
      _          <- data.log.info("Deleted schema")
    } yield ()
  }
}
