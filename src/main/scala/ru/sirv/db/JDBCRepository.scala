package ru.sirv.db

import ru.sirv.domain.{User, UserMeta}

import java.sql.{Connection, ResultSet}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class JDBCRepository (connection: Connection) extends DbRepository {
  def insertUser(user: User): Future[Unit] = {
    val sql =
      s"""
         |insert into "User" (email, name, age) values ('${user.email}', '${user.name}', ${user.age})
         |""".stripMargin
         val statement = connection.createStatement()
    val isSuccess = statement.execute(sql)
    if (isSuccess) Future(println("User inserted"))
    else Future.failed(new RuntimeException("User not inserted"))
  }

  def selectUser(email: String): Future[Option[User]] = Future {
    val selectsql =
      s"""
         |select * from "User" where email='${email}'
         |""".stripMargin
         val statement = connection.createStatement()
    val resultSet: ResultSet = statement.executeQuery(selectsql)
    val builder = List.newBuilder[User]
    while (resultSet.next()) {
      val email = resultSet.getString("email")
      val name = resultSet.getString("name")
      val age = resultSet.getInt("age")
      val user = User(email, name, age)
      builder.addOne(user)
    }

    val users = builder.result()
    users.headOption
  }

  def updateUser(user: User): Future[Unit] = {
    val updatesql =
      s"""
        |UPDATE "User"
        |SET email = '${user.email}', name = '${user.name}', age = ${user.age}
        |WHERE email = ${user.email};
        |""".stripMargin
        val statement = connection.createStatement()
    val isSuccess = statement.executeUpdate(updatesql)
    if (isSuccess==0) Future.failed(new RuntimeException("Update failed"))
    else Future(println("Updated successfully"))
  }

  def deleteUser(email: String): Future[Unit] = {
    val deletesql =
      s"""
         |delete from "User" where email=${email}
         |""".stripMargin
         val statement = connection.createStatement()
    val isSuccess = statement.executeUpdate(deletesql)
    if (isSuccess!=0) Future(println("Deleted successfully"))
    else Future.failed(new RuntimeException("Delete failed"))
  }

  def insertUserMeta(user: UserMeta): Future[Unit] = ???

  def selectUserMeta(email: String): Future[Option[UserMeta]]= ???

  def updateUserMeta(user: UserMeta): Future[Unit] = ???

  def deleteUserMeta(email: String): Future[Unit] = ???
}
