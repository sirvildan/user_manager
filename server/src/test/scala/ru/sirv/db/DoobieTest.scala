package ru.sirv.db

import cats.effect.unsafe.implicits._
import cats.effect.{IO, Resource}
import cats.syntax.all._
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.typelevel.log4cats.SelfAwareStructuredLogger
import org.typelevel.log4cats.slf4j._
import ru.sirv.db.DbModule.{ConnectionConfig, PoolConfig}
import ru.sirv.domain.Userinfo

import java.util.UUID
import scala.concurrent.duration._

class DoobieTest extends AnyFunSuite with BeforeAndAfterAll {

  implicit val log: SelfAwareStructuredLogger[IO] = Slf4jLogger.getLogger[IO]

  val config: ConnectionConfig = ConnectionConfig("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/sirvildan_db", "postgres", "internet")
  val poolConfig: PoolConfig = PoolConfig(8, 1, 2.minutes, 30.seconds.some, 5.minutes.some, 8)
  val dbModule: DbModule = new DbModule()
  def dbService: Resource[IO, DbService[IO]] = dbModule.buildService(config, poolConfig, "DoobieTest")

  override protected def beforeAll(): Unit = {
    dbModule.migrate(config, Seq("classpath:/migrations/postgres"))
    super.beforeAll()
  }

  test("Userinfo should be inserted") {
    val email = UUID.randomUUID().toString + "testing1@mail"
    val user = Userinfo(email, "nametest", 21)

    val result = dbService.use{service =>
      service.createUser(user) *> service.readUser(user.email)
    }.unsafeRunSync()

    assert(result.nonEmpty && result.get.email == email)
  }
}