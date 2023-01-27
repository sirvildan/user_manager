import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource}
import cats.implicits.catsSyntaxOptionId
import com.dimafeng.testcontainers.PostgreSQLContainer
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import org.scalatest.flatspec.AnyFlatSpec
import org.testcontainers.utility.DockerImageName
import org.typelevel.log4cats.slf4j.loggerFactoryforSync
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}
import ru.sirv.db.DbModule.{ConnectionConfig, PoolConfig}
import ru.sirv.db.{DbModule, DbService}
import ru.sirv.domain.{Userinfo, UserMeta}

import scala.concurrent.duration.DurationInt

class UserManagerTest extends AnyFlatSpec with TestContainerForAll {
  implicit val loggerCats: SelfAwareStructuredLogger[IO] = LoggerFactory.getLoggerFromName[IO]("UserManagerTest")
  override val containerDef = PostgreSQLContainer.Def(
    dockerImageName = DockerImageName.parse("postgres:15.1"),
    databaseName = "testcontainer-scala",
    username = "scala",
    password = "scala"
  )

  "UserManager" should "work correctly" in {
    withContainers { pgContainer =>
      val config = DbModule.Config(
        DbModule.PostgresConfig(
          ConnectionConfig(pgContainer.driverClassName, pgContainer.jdbcUrl, pgContainer.username, pgContainer.password),
          PoolConfig(8, 1, 30.seconds, 60.seconds.some, 5.minutes.some, 8)
        ),
        "classpath:/migrations/postgres")

      val dbModule = new DbModule()

      dbModule.migrate(config.postgres.connection, Seq(config.migrationLocation)).unsafeRunSync()

      val serviceResource: Resource[IO, DbService[IO]] =
        dbModule.buildService(config.postgres.connection, config.postgres.commonPool, "TestPool")

      //CREATE USER
      serviceResource.use { dbService =>
        dbService.createUser(Userinfo("1@email.ru", "1", 1))
      }.unsafeRunSync()

      val user1 = serviceResource.use(serv => serv.readUser("1@email.ru")).unsafeRunSync()
      assert(user1.nonEmpty)


      //UPDATE USER
      serviceResource.use { dbService =>
        dbService.createUser(Userinfo("upd@mail.ru", "old_name", 1)) *>
          dbService.updateUser(Userinfo("upd@mail.ru", "new_name", 55))
      }.unsafeRunSync()

      val user2 = serviceResource.use(serv => serv.readUser("upd@mail.ru")).unsafeRunSync()
      assert(user2.get.name == "new_name" && user2.get.age == 55.some)

      //DELETE USER
      serviceResource.use { dbService =>
        dbService.createUser(Userinfo("delete@mail.ru", "delete", 1)) *>
          dbService.deleteUser("delete@mail.ru")
      }.unsafeRunSync()

      val user3 = serviceResource.use(serv => serv.readUser("delete@mail.ru")).unsafeRunSync()
      assert(user3.isEmpty)

      //CREATE USERMETA
      serviceResource.use { dbService =>
        dbService.createUser(Userinfo("meta@mail.ru", "formeta", 1)) *>
          dbService.createUserMeta(UserMeta("meta@mail.ru", "fishing", List("Friend1@mail.ru", "Friend2@mail.ru")))
      }.unsafeRunSync()

      val meta1 = serviceResource.use(serv => serv.readUserMeta("meta@mail.ru")).unsafeRunSync()
      assert(meta1.nonEmpty)

      //UPDATE USERMETA
      serviceResource.use { dbService =>
        dbService.createUser(Userinfo("updatemeta@mail.ru", "updatemeta", 3)) *>
          dbService.createUserMeta(UserMeta("updatemeta@mail.ru", "old_hobby", List("OldFriend1@mail.ru", "OldFriend2@mail.ru"))) *>
            dbService.updateUserMeta(UserMeta("updatemeta@mail.ru", "new_hobby", List("NewFriend1@mail.ru", "NewFriend2@mail.ru")))
      }.unsafeRunSync()

      val metaupd = serviceResource.use(serv => serv.readUserMeta("updatemeta@mail.ru")).unsafeRunSync()
      assert(metaupd.get.hobby == "new_hobby" && metaupd.get.friendsEmails == List("NewFriend1@mail.ru", "NewFriend2@mail.ru"))

      //DELETE USERMETA
      serviceResource.use { dbService =>
        dbService.createUser(Userinfo("deletemeta@mail.ru", "deletemeta", 2)) *>
          dbService.createUserMeta(UserMeta("deletemeta@mail.ru", "delete", List("Delete1@mail.ru"))) *>
            dbService.deleteUserMeta("deletemeta@mail.ru")
      }.unsafeRunSync()

      val metadel = serviceResource.use(serv => serv.readUserMeta("deletemeta@mail.ru")).unsafeRunSync()
      assert(metadel.isEmpty)
    }
  }
}