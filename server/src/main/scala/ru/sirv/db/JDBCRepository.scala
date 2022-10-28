package ru.sirv.db

import java.sql.{Connection, ResultSet}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import ru.sirv.domain._

class JDBCRepository(connection: Connection) extends DbRepository[Future] {
  def insertUser(userinfo: Userinfo): Future[Unit] = Future {
    val sql =
      s"""
         |insert into "Userinfo" (email, name, age) values ('${userinfo.email}', '${userinfo.name}', ${userinfo.age.getOrElse(0)})
         |""".stripMargin // FIXME getOrElse for age
    val statement = connection.createStatement()
    statement.execute(sql)
    println("Userinfo inserted")
  }

  def selectUser(email: String): Future[Option[Userinfo]] = Future {
    val selectsql =
      s"""
         |select * from "Userinfo" where email='${email}'
         |""".stripMargin
    val statement = connection.createStatement()
    val resultSet: ResultSet = statement.executeQuery(selectsql)
    val builder = List.newBuilder[Userinfo]
    while (resultSet.next()) {
      val email = resultSet.getString("email")
      val name = resultSet.getString("name")
      val age = resultSet.getInt("age")
      val userinfo = Userinfo(email, name, age)
      builder.addOne(userinfo)
    }

    val users = builder.result()
    users.headOption
  }

  def updateUser(userinfo: Userinfo): Future[Unit] = {
    val updatesql =
      s"""
         |UPDATE "Userinfo"
         |SET email = '${userinfo.email}', name = '${userinfo.name}', age = ${userinfo.age.getOrElse(0)}
         |WHERE email = '${userinfo.email}'
         |""".stripMargin // FIXME option age
    val statement = connection.createStatement()
    val isSuccess = statement.executeUpdate(updatesql)
    if (isSuccess == 0) Future.failed(new RuntimeException("Update failed"))
    else Future(println("Updated successfully"))
  }

  def deleteUser(email: String): Future[Unit] = {
    val deletesql =
      s"""
         |delete from "Userinfo" where email='${email}'
         |""".stripMargin
    val statement = connection.createStatement()
    val deletedRows = statement.executeUpdate(deletesql)
    if (deletedRows > 0) Future.successful(println("Deleted successfully"))
    else Future.failed(new RuntimeException("Delete failed"))
  }

  def insertUserMeta(userMeta: UserMeta): Future[Unit] = Future {
    val sql =
      s"""
         |insert into UserMeta (email, hobby, friendsemail) values ('${userMeta.email}', '${userMeta.hobby}', '${userMeta.friendsEmails}')
         |""".stripMargin // FIXME getOrElse for age
    val statement = connection.createStatement()
    statement.execute(sql)
    println("Usermeta inserted")
  }

  def selectUserMeta(email: String): Future[Option[UserMeta]] = Future {
    val selectsql =
      s"""
         |select * from UserMeta where email='${email}'
         |""".stripMargin
    val statement = connection.createStatement()
    val resultSet: ResultSet = statement.executeQuery(selectsql)
    val builder = List.newBuilder[UserMeta]
    while (resultSet.next()) {
      val email = resultSet.getString("email")
      val hobby = resultSet.getString("hobby")
      val friendsemail = resultSet.getString("friendsemail") //////////////////////////Seq
      val userMeta = UserMeta(email, hobby, List(friendsemail))
      builder.addOne(userMeta)
    }
    val usersMeta = builder.result()
    usersMeta.headOption
  }

  def updateUserMeta(userMeta: UserMeta): Future[Unit] = {
    val updatesql =
      s"""
         |UPDATE UserMeta
         |SET email = '${userMeta.email}', hobby = '${userMeta.hobby}', friendsemail = '${userMeta.friendsEmails}'
         |WHERE email = '${userMeta.email}'
         |""".stripMargin // FIXME option age
    val statement = connection.createStatement()
    val isSuccess = statement.executeUpdate(updatesql)
    if (isSuccess == 0) Future.failed(new RuntimeException("Update meta failed"))
    else Future(println("Meta updated successfully"))
  }

  def deleteUserMeta(email: String): Future[Unit] = {
    val deletesql =
      s"""
         |delete from UserMeta where email='${email}'
         |""".stripMargin
    val statement = connection.createStatement()
    val isSuccess = statement.executeUpdate(deletesql)
    if (isSuccess != 0) Future(println("Meta deleted successfully"))
    else Future.failed(new RuntimeException("Meta delete failed"))
  }

  def addFriendEmail(email: String, appendEmail: String): Future[Unit] = { // FIXME MULTI VALUE ADDITION
    val addEmail =
      s"""
        UPDATE UserMeta
        SET friendsemail = array_append(friendsemail, '${appendEmail}') where email = '${email}'
        """.stripMargin
    val statement = connection.createStatement()
    val updated = statement.executeUpdate(addEmail)
    if (updated > 0) Future.successful(println("Added to array successfully"))
    else Future.failed(new RuntimeException("Add to array failed"))
  }

  def deleteFriendEmail(email: String, removeEmail: String): Future[Unit] = {
    val deleteEmail =
      s"""
        UPDATE UserMeta
        set friendsemail = array_remove(friendsemail, '${removeEmail}') where email = '${email}'
        """.stripMargin
    val statement = connection.createStatement()
    val updated = statement.executeUpdate(deleteEmail)
    if (updated > 0) Future.successful(println("Deleted from array successfully"))
    else Future.failed(new RuntimeException("Delete from array failed"))
  }
}