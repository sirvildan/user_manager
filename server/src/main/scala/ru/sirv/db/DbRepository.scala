package ru.sirv.db

import ru.sirv.domain.{User, UserMeta}

import scala.concurrent.Future

trait DbRepository {
  def insertUser(user: User): Future[Unit]
  def selectUser(email: String): Future[Option[User]]
  def updateUser(user: User): Future[Unit]
  def deleteUser(email: String): Future[Unit]

//  -----------------------------------------------------------

  def insertUserMeta(user: UserMeta): Future[Unit]
  def selectUserMeta(email: String): Future[Option[UserMeta]]
  def updateUserMeta(user: UserMeta): Future[Unit]
  def deleteUserMeta(email: String): Future[Unit]
}