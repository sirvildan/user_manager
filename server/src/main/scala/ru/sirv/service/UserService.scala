package ru.sirv.service

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import org.typelevel.log4cats.{Logger, LoggerFactory}
import org.typelevel.log4cats.slf4j._
import ru.sirv.db.DbService
import ru.sirv.domain._

class UserService(dbService: DbService[IO]){
  implicit val logger: Logger[IO] = LoggerFactory.getLoggerFromName[IO]("UserService")
  def getUser(email: String): IO[Userinfo] = {
    logger.info(s"Get userinfo $email") *>
    dbService.readUser(email).flatMap {
      case Some(value) =>
        value.pure[IO]
      case None =>
        IO.raiseError(new NoSuchElementException(s"User not found by [$email]"))
    }
  }

  def deleteUser(email: String): IO[Unit] = {
    logger.info(s"Delete userinfo by $email") *>
    dbService.deleteUser(email)
  }

  def addUser(userinfo: Userinfo): IO[Unit] = {
    logger.info(s"Add userinfo $userinfo") *>
    dbService.createUser(userinfo)
  }

  def updateUser(userinfo: Userinfo): IO[Unit] = {
    logger.info(s"Update userinfo $userinfo") *>
    dbService.updateUser(userinfo)
  }

  def getUserMeta(email: String): IO[UserMeta] = {
    logger.info(s"Get usermeta by id $email") *>
    dbService.readUserMeta(email).flatMap {
      case Some(value) =>
        value.pure[IO]
      case None =>
        IO.raiseError(new NoSuchElementException(s"User not found by [$email]"))
    }
  }

  def deleteUserMeta(email: String): IO[UserMeta] = {
    logger.info(s"Delete userinfo by $email") *>
    dbService.readUserMeta(email).flatMap {
      case Some(value) =>
        dbService.deleteUserMeta(email).as(value)
      case None =>
        IO.raiseError(new NoSuchElementException(s"User not found by $email"))
    }
  }

  def addUserMeta(userMeta: UserMeta): IO[Unit] = {
    logger.info(s"Add userinfo $userMeta") *>
    dbService.createUserMeta(userMeta)
  }

  def updateUserMeta(userMeta: UserMeta): IO[Unit] = {
    logger.info(s"Update userinfo $UserMeta") *>
    dbService.updateUserMeta(userMeta)
  }
}