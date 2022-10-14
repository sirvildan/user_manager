package ru.sirv.service

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import com.typesafe.scalalogging.Logger
import ru.sirv.domain._

import java.util.UUID

class UserService{
  implicit val logger = Logger("UserService")
  def getUser(userId: UUID): IO[User] = {
    logger.info(s"Get user by id $userId")
    IO.pure(User("ss@mal.ru", "mock", None))
  }
  def getAllUsers: IO[Seq[User]] = {
    logger.info("Get all users")
    Seq(User("mail1@k","Name1",12), User("mail2@k","Name2",14)).pure[IO]
  }

  def deleteUser(userId: UUID): IO[User] = {
    logger.info(s"Delete user by $userId")
    IO.pure(User("ss@mal.ru", "mock", None))
  }
  def addUser(User: User): IO[UUID] = {
    logger.info(s"Add user $User")
    IO.pure(UUID.randomUUID())
  }
  def updateUser(User: User, userId: UUID): IO[Unit] = {
    logger.info(s"Update user $User id=$userId")
    IO.unit
  }
}