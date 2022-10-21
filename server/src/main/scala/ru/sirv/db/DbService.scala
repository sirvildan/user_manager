package ru.sirv.db

import cats.effect.{IO, Resource}
import doobie.implicits._
import doobie.util.transactor.Transactor
import ru.sirv.domain._

trait DbService[F[_]] {
  def createUser(userinfo: Userinfo): F[Unit]
  def readUser(email: String): F[Option[Userinfo]]
  def updateUser(userinfo: Userinfo): F[Unit]
  def deleteUser(email: String): F[Unit]

//------------------------------------------------------

  def createUserMeta(userinfo: UserMeta): F[Unit]
  def readUserMeta(email: String): F[Option[UserMeta]]
  def updateUserMeta(userinfo: UserMeta): F[Unit]
  def deleteUserMeta(email: String): F[Unit]
  def addEmailFriend(email: String): F[Unit]
  def deleteEmailFriend(email: String): F[Unit]
}

object DbService {
  def impl(xa: Transactor[IO], accessor: DoobieAccessor): Resource[IO, DbService[IO]] =
    Resource.pure{
      new DbService[IO] {
        override def createUser(userinfo: Userinfo): IO[Unit] =
          accessor.insertUser(userinfo).transact(xa)

        override def readUser(email: String): IO[Option[Userinfo]] =
          accessor.selectUser(email).transact(xa)

        override def updateUser(userinfo: Userinfo): IO[Unit] = ???

        override def deleteUser(email: String): IO[Unit] = ???

        override def createUserMeta(userinfo: UserMeta): IO[Unit] = ???

        override def readUserMeta(email: String): IO[Option[UserMeta]] = ???

        override def updateUserMeta(userinfo: UserMeta): IO[Unit] = ???

        override def deleteUserMeta(email: String): IO[Unit] = ???

        override def addEmailFriend(email: String): IO[Unit] = ???

        override def deleteEmailFriend(email: String): IO[Unit] = ???
      }
    }
}