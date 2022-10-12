package ru.sirv.db
import ru.sirv.domain._
import scala.concurrent.Future

trait DbService {
  def createUser(user: User): Future[Unit]
  def readUser(email: String): Future[Option[User]]
  def updateUser(user: User): Future[Unit]
  def deleteUser(email: String): Future[Unit]

//------------------------------------------------------

  def createUserMeta(user: UserMeta): Future[Unit]
  def readUserMeta(email: String): Future[Option[UserMeta]]
  def updateUserMeta(user: UserMeta): Future[Unit]
  def deleteUserMeta(email: String): Future[Unit]
  def addEmailFriend(email: String): Future[Unit]
  def deleteEmailFriend(email: String): Future[Unit]
}