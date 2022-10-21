package ru.sirv.db
import ru.sirv.domain._

trait DbRepository[F[_]] {
  def insertUser(userinfo: Userinfo): F[Unit]
  def selectUser(email: String): F[Option[Userinfo]]
  def updateUser(userinfo: Userinfo): F[Unit]
  def deleteUser(email: String): F[Unit]

//  -----------------------------------------------------------

  def insertUserMeta(userinfo: UserMeta): F[Unit]
  def selectUserMeta(email: String): F[Option[UserMeta]]
  def updateUserMeta(userinfo: UserMeta): F[Unit]
  def deleteUserMeta(email: String): F[Unit]
}