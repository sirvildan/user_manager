package ru.sirv.db

import org.scalatest.funsuite.AnyFunSuite
import ru.sirv.db.DbModule.JdbcDatabaseConfig

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class JDBCTest extends AnyFunSuite {
  val config = JdbcDatabaseConfig()
  val dbModule = new DbModule(config)
  test("Migrations should execute") {
    val migrateResultIsSuccess: Boolean = Await.result(dbModule.migrate, 2.seconds)
    assert(migrateResultIsSuccess)
  }
}