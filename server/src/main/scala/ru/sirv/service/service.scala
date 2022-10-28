package ru.sirv.service

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import com.typesafe.scalalogging.Logger
import ru.sirv.domain._
import java.util.UUID

class UserService{
  implicit val logger = Logger("UserService")
  def getUser(userId: UUID): IO[Userinfo] = {
    logger.info(s"Get userinfo by id $userId")
    IO.pure(Userinfo("ss@mal.ru", "mock", None))
  }
  def getAllUsers: IO[Seq[Userinfo]] = {
    logger.info("Get all users")
    Seq(Userinfo("mail1@k","Name1",12), Userinfo("mail2@k","Name2",14)).pure[IO]
    //Seq(UserMeta("metamail@met", "hobbytest", "mail1,mail2")).pure[IO]
  }

  def deleteUser(userId: UUID): IO[Userinfo] = {
    logger.info(s"Delete userinfo by $userId")
    IO.pure(Userinfo("ss@mal.ru", "mock", None))
  }
  def addUser(Userinfo: Userinfo): IO[UUID] = {
    logger.info(s"Add userinfo $Userinfo")
    IO.pure(UUID.randomUUID())
  }
  def updateUser(Userinfo: Userinfo, userId: UUID): IO[Unit] = {
    logger.info(s"Update userinfo $Userinfo id=$userId")
    IO.unit
  }

  def getAllMeta: IO[Seq[UserMeta]] = {
    logger.info("Get all usermeta")
    Seq(UserMeta("metamail@met", "hobbytest", List("mail1", "mail2")), UserMeta("metamail22@aa.ru", "hobby22",List("mail3", "mail4"))).pure[IO]
  }

  def getUserMeta(usermetaId: UUID): IO[UserMeta] = {
    logger.info(s"Get userinfo by id $usermetaId")
    IO.pure(UserMeta("meta@mail", "fishing", List("mail1", "mail2")))
  }

  def deleteUserMeta(usermetaId: UUID): IO[UserMeta] = {
    logger.info(s"Delete userinfo by $usermetaId")
    IO.pure(UserMeta("meta@mail", "fishing", List("mail1", "mail2")))
  }
  def addUserMeta(UserMeta: UserMeta): IO[UUID] = {
    logger.info(s"Add userinfo $UserMeta")
    IO.pure(UUID.randomUUID())
  }
  def updateUserMeta(UserMeta: UserMeta, usermetaId: UUID): IO[Unit] = {
    logger.info(s"Update userinfo $UserMeta id=$usermetaId")
    IO.unit
  }
}