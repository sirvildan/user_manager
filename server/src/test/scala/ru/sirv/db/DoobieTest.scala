package ru.sirv.db

import cats.effect.unsafe.implicits._
import cats.effect.{ IO, Resource }
import cats.syntax.all._
import org.scalatest.{ BeforeAndAfterAll, Ignore }
import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j._
import ru.sirv.db.DbModule._
import ru.sirv.domain.{ UserMeta, Userinfo }

import java.util.UUID
import scala.concurrent.duration._

@Ignore
class DoobieTest extends AnyFunSuite with BeforeAndAfterAll {

  implicit val log: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  val config: ConnectionConfig =
    ConnectionConfig("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/sirvildan_db", "postgres", "internet")
  val poolConfig: PoolConfig = PoolConfig(8, 1, 2.minutes, 30.seconds.some, 5.minutes.some, 8)
  val dbModule: DbModule     = new DbModule()

  def dbService: Resource[IO, DbService[IO]] = dbModule.buildService(config, poolConfig, "DoobieTest")

  override protected def beforeAll(): Unit = {
    dbModule.migrate(config, Seq("classpath:/migrations/postgres"))
    super.beforeAll()
  }

  test("Userinfo should be inserted") {
    val email = UUID.randomUUID().toString + "||| INSERTINFO"
    val user  = Userinfo(email, "nametest", 21)
    val result = dbService
      .use { service =>
        service.createUser(user) *> service.readUser(user.email)
      }
      .unsafeRunSync()
    assert(result.nonEmpty && result.get.email == email)
  }

  test("Userinfo should be updated") {
    val rand     = new scala.util.Random
    val email    = UUID.randomUUID().toString + "||| UPDATEINFO"
    val new_name = "UPDATEDNAME"
    val new_age  = rand.between(0, 100)
    val user     = Userinfo(email, "newnametest", 31)
    val newuser  = Userinfo(email, new_name, new_age)
    val result = dbService
      .use { service =>
        service.createUser(user) *>
        service.updateUser(newuser) *>
        service.readUser(newuser.email)
      }
      .unsafeRunSync()
    assert(result.get.email == email && result.get.name == new_name)
  }

  test("Userinfo should be deleted") {
    val email = UUID.randomUUID().toString + "||| DELETEINFO"
    val user  = Userinfo(email, "new", 1)
    val result: Option[Userinfo] = dbService
      .use { service =>
        service.createUser(user) *>
        service.deleteUser(user.email) *>
        service.readUser(user.email)
      }
      .unsafeRunSync()
    assert(result.isEmpty)
  }

  test("Usermeta should be selected") {
    val email = UUID.randomUUID().toString + "||| SELECTMETA"
    val user  = Userinfo(email, "nametest", 21)
    val meta  = UserMeta(email, "hobby1", List("a", "b", "c"))
    val result = dbService
      .use { service =>
        service.createUser(user) *> service.createUserMeta(meta) *> service.readUserMeta(email)
      }
      .unsafeRunSync()
    assert(result.nonEmpty && result.get.email == email)
  }

  test("Usermeta should be inserted") {
    val email = UUID.randomUUID().toString + "||| INSERTMETA"
    val user  = Userinfo(email, "nametest", 21)
    val meta  = UserMeta(email, "hobby1", List("a", "b", "c"))
    val result = dbService
      .use { service =>
        service.createUser(user) *> service.createUserMeta(meta) *> service.readUserMeta(email)
      }
      .unsafeRunSync()
    assert(result.nonEmpty && result.get.email == email)
  }

  test("Usermeta should be deleted") {
    val email = UUID.randomUUID().toString + "||| DELETEMETA"
    val user  = Userinfo(email, "deletetest", 55)
    val meta  = UserMeta(email, "delete", List("delete", "this"))
    val result = dbService
      .use { service =>
        service.createUser(user) *>
        service.createUserMeta(meta) *>
        service.deleteUserMeta(meta.email) *>
        service.readUserMeta(meta.email)
      }
      .unsafeRunSync()
    assert(result.isEmpty)
  }

  test("Usermeta should be updated") {
    val email     = UUID.randomUUID().toString + "||| UPDATEMETA"
    val user      = Userinfo(email, "updatemeta", 31)
    val meta      = UserMeta(email, "updatemeta", List("UPDATE", "this"))
    val new_hobby = "new_hobby"
    val new_meta  = UserMeta(email, new_hobby, List("Updated", "list"))
    val result = dbService
      .use { service =>
        service.createUser(user) *>
        service.createUserMeta(meta) *>
        service.updateUserMeta(new_meta) *>
        service.readUserMeta(new_meta.email)
      }
      .unsafeRunSync()
    assert(result.nonEmpty && result.get.hobby == new_hobby)
  }

  test("Friend's email should be added") {
    val email       = "add"
    val friendEmail = "fintest"
    val result = dbService
      .use { service =>
        service.addEmailFriend(email, friendEmail) *>
        service.readUserMeta(email)
      }
      .unsafeRunSync()
    assert(result.isDefined && result.get.friendsEmails.contains(friendEmail))
  }

  test("Friend's email should be deleted") {
    val email       = "add"
    val friendEmail = "fintest"
    val result: Option[UserMeta] = dbService
      .use { service =>
        service.deleteEmailFriend(email, friendEmail) *>
        service.readUserMeta(email)
      }
      .unsafeRunSync()
    assert(result.isDefined && result.get.friendsEmails.exists(_ != friendEmail))
  }
}
