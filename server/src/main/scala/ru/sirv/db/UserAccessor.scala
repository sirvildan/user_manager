package ru.sirv.db

import cats.syntax.all._
import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import ru.sirv.domain.{UserMeta, Userinfo}
import ru.sirv.db._

import java.util.UUID

class UserAccessor extends DbRepository[ConnectionIO] {

  def insertUser(userinfo: Userinfo): ConnectionIO[Unit] = {
    val id = UUID.randomUUID()
    val sql =
      sql"""INSERT INTO userinfo(id, email, name, age)
           |VALUES ($id, ${userinfo.email}, ${userinfo.name}, ${userinfo.age})
           |""".stripMargin

    sql.update.run.void
  }

  override def selectUserById(id: UUID): doobie.ConnectionIO[Option[Userinfo]] = {
    val query =
      sql"""SELECT email,name,age
           |FROM userinfo
           |WHERE id = $id""".stripMargin

    query
      .query[(String, Option[String], Option[Int])]
      .option.map {
      case Some((e, Some(n), a)) => Userinfo(e, n, a).some
      case _ => none
    }
  }

  override def selectUser(email: String): ConnectionIO[Option[Userinfo]] = {
    val query =
      sql"""SELECT email,name,age
           |FROM userinfo
           |WHERE email = $email""".stripMargin

    query
      .query[(String, Option[String], Option[Int])]
      .option.map {
      case Some((e, Some(n), a)) => Userinfo(e, n, a).some
      case _ => none
    }
  }


  override def updateUser(userinfo: Userinfo): doobie.ConnectionIO[Unit] = {
    val sql =
      sql"""UPDATE userinfo
           |SET email = ${userinfo.email}, name = ${userinfo.name}, age = ${userinfo.age}
           |WHERE email = ${userinfo.email}""".stripMargin
    sql.update.run.void
  }

  override def deleteUser(email: String): doobie.ConnectionIO[Unit] = {
    val sql =
      sql"""DELETE FROM userinfo
           |WHERE email=$email"""
    sql.update.run.void
  }

  override def insertUserMeta(usermeta: UserMeta): doobie.ConnectionIO[Unit] = {
    val sql =
      sql"""INSERT INTO usermeta(email, hobby, friendsemail)
           |VALUES (${usermeta.email}, ${usermeta.hobby}, ${usermeta.friendsEmails})
           |""".stripMargin
    sql.update.run.void
  }

  override def selectUserMeta(email: String): doobie.ConnectionIO[Option[UserMeta]] = {
    val query =
      sql"""SELECT email,hobby,friendsemail
           |FROM usermeta
           |WHERE email = ${email}""".stripMargin
    query
      .query[(String, String, List[String])]
      .option.map {
      case Some((e, n, a)) => UserMeta(e, n, a).some
      case _ => none
    }
  }

  override def updateUserMeta(usermeta: UserMeta): doobie.ConnectionIO[Unit] = {
    val sql =
      sql"""UPDATE usermeta
           |SET email = ${usermeta.email}, hobby = ${usermeta.hobby}, friendsemail = ${usermeta.friendsEmails}
           |WHERE email = ${usermeta.email}""".stripMargin
    sql.update.run.void
  }

  override def deleteUserMeta(email: String): doobie.ConnectionIO[Unit] = {
    val sql =
      sql"""DELETE from usermeta
           |WHERE email=$email"""
    sql.update.run.void
  }


  def addEmailFriend(email: String, friendEmail: String): doobie.ConnectionIO[Unit] = {
    val sql =
      sql"""UPDATE usermeta
           |SET friendsemail = array_append(friendsemail, $friendEmail)
           |WHERE email = $email"""
    sql.update.run.void
  }

  def deleteEmailFriend(email: String, friendEmail: String): doobie.ConnectionIO[Unit] = {
    val sql =
      sql"""UPDATE usermeta
           |SET friendsemail = array_remove(friendsemail, $friendEmail)
           |WHERE email = $email"""
      sql.update.run.void
  }
}
