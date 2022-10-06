package ru.sirv.db

import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import ru.sirv.db.DbModule.JdbcDatabaseConfig
import ru.sirv.domain.{User, UserMeta}

import java.sql.Connection
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class JDBCUserTest extends AnyFunSuite with BeforeAndAfterAll {
  val config = JdbcDatabaseConfig()
  val dbModule = new DbModule(config)
  var connection = Option.empty[Connection]
  val random = Random
  val email = "smthtwo@mail.com"

  override protected def beforeAll(): Unit = {
    connection = Some(dbModule.getConnection())
    super.beforeAll()
  }

  test("Migrations should execute") {
    val migrateResultIsSuccess: Boolean = Await.result(dbModule.migrate, 2.seconds)
    assert(migrateResultIsSuccess)
  }
  test("User should be inserted") {
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    //val email = random.nextDouble() + "@outlook.com"
    val email = "testing1@mail"
    Await.result(dbRepository.insertUser(User(email, "nametest", 21)), 200.seconds)
    val result: Option[User] = Await.result(dbRepository.selectUser(email), 200.seconds)
    assert(result.nonEmpty && result.get.email == email)
  }

  test("User should be deleted") {
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    val email = "smththree@mail.com"
    Await.result(dbRepository.insertUser(User(email, "nametest", 21)), 200.seconds)
    val isSuccess = Await.result(
      dbRepository
        .deleteUser(email)
        .map(_ => true)
        .recover(_ => false)
      , 200.seconds)
    assert(isSuccess)
  }

  test("User should be selected") {
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    val result: Option[User] = Await.result(dbRepository.selectUser(email), 200.seconds)
    assert(result.nonEmpty && result.get.email == email)
  }

  test("User should be updated") {
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    Await.result(dbRepository.updateUser(User(email,"name",22)),200.seconds)
    val result: Option[User] = Await.result(dbRepository.selectUser(email),200.seconds)
    assert(result.nonEmpty && result.get.email == email)
  }

  test("UserMeta should be inserted") {
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    //val email = random.nextDouble() + "@outlook.com"
    val email = "testing1@mail"
    //Await.result(dbRepository.insertUserMeta(UserMeta(email, "hobbynew", Seq("{f1@mail.ru}"))), 200.seconds)
    Await.result(dbRepository.insertUserMeta(UserMeta(email, "hobbynew", "{f1@mail.ru, f2@mail.ru}")), 200.seconds)
    val result: Option[UserMeta] = Await.result(dbRepository.selectUserMeta(email), 200.seconds)
    assert(result.nonEmpty && result.get.email == email)
  }

  test("UserMeta should be deleted") {
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    val email = "smthone"
    //Await.result(dbRepository.insertUserMeta(UserMeta(email, "hobbydeletetest", Seq("ff"))), 200.seconds)
    Await.result(dbRepository.insertUserMeta(UserMeta(email, "hobbydeletetest", "{ff}")), 200.seconds)
    val isSuccess = Await.result(
      dbRepository
        .deleteUserMeta(email)
        .map(_ => true)
        .recover(_ => false)
      , 200.seconds)
    assert(isSuccess)
  }

  test("UserMeta should be updated") {
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    Await.result(dbRepository.updateUserMeta(UserMeta("smthone","hobhupdtes", "{ff}")),200.seconds)
    val result: Option[UserMeta] = Await.result(dbRepository.selectUserMeta(email),200.seconds)
    assert(result.nonEmpty && result.get.email == email)
  }

  test("UserMeta should be selected") {
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    val result: Option[UserMeta] = Await.result(dbRepository.selectUserMeta(email), 200.seconds)
    assert(result.nonEmpty && result.get.email == email)
  }

  test("Value should be added into array friendsemail") {                   // FIXME MULTI VALUE ADDITION
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    val email = "testing@m"
    //Await.result(dbRepository.addFriendEmail(UserMeta(email, Seq("{f1@mail.ru}"))), 200.seconds)
    //Await.result(dbRepository.insertUserMeta(UserMeta(email, "addarraytest", "ff")), 200.seconds)
    Await.result(dbRepository.addFriendEmail(email, "someaddemail@kek.ru"), 200.seconds)
    val result: Option[UserMeta] = Await.result(dbRepository.selectUserMeta(email), 200.seconds)
    assert(result.nonEmpty && result.get.email == email)
  }

  test("Value should be deleted from array friendsemail") {
    assert(connection.nonEmpty)
    val con = connection.get
    val dbRepository = new JDBCRepository(con)
    val email = "testing@m"
    //Await.result(dbRepository.addFriendEmail(UserMeta(email, Seq("{f1@mail.ru}"))), 200.seconds)
    //Await.result(dbRepository.insertUserMeta(UserMeta(email, "addarraytest", "ff")), 200.seconds)
    Await.result(dbRepository.deleteFriendEmail(email, "someaddemail@kek.ru"), 200.seconds)
    val result: Option[UserMeta] = Await.result(dbRepository.selectUserMeta(email), 200.seconds)
    assert(result.nonEmpty && result.get.email == email)
  }

  override protected def afterAll(): Unit = {
    connection.foreach(_.close())
    super.afterAll()
  }
}