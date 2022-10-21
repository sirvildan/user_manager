package ru.sirv.db

import cats.syntax.all._
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import ru.sirv.domain.{UserMeta, Userinfo}

class DoobieAccessor extends DbRepository[ConnectionIO] {

  def insertUser(userinfo: Userinfo): ConnectionIO[Unit] = {
    val sql =
      sql"""INSERT INTO userinfo(email, name, age)
           |VALUES (${userinfo.email}, ${userinfo.name}, ${userinfo.age})
           |""".stripMargin

    sql.update.run.void
  }


  override def selectUser(email: String): ConnectionIO[Option[Userinfo]] = ???


  override def updateUser(userinfo: Userinfo): doobie.ConnectionIO[Unit] = ???

  override def deleteUser(email: String): doobie.ConnectionIO[Unit] = ???

  override def insertUserMeta(userinfo: UserMeta): doobie.ConnectionIO[Unit] = ???

  override def selectUserMeta(email: String): doobie.ConnectionIO[Option[UserMeta]] = ???

  override def updateUserMeta(userinfo: UserMeta): doobie.ConnectionIO[Unit] = ???

  override def deleteUserMeta(email: String): doobie.ConnectionIO[Unit] = ???

  def initUser(userId: String): ConnectionIO[Int] = {
    val sql =
      sql"""INSERT INTO quota_user_rules(user_id)
           |VALUES ($userId)
           |""".stripMargin

    sql.update.run
  }

  def getUserGroups(userId: String): ConnectionIO[List[String]] = {
    val query =
      sql"""SELECT groups
           |FROM quota_user_rules
           |WHERE user_id = $userId""".stripMargin

    query.query[Option[List[String]]].unique.map(_.getOrElse(List.empty))
  }
}
