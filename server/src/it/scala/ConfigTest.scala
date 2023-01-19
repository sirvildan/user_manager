import com.dimafeng.testcontainers.PostgreSQLContainer
import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import org.scalatest.flatspec.AnyFlatSpec
import org.testcontainers.utility.DockerImageName

import java.sql.DriverManager

class ConfigTest extends AnyFlatSpec with TestContainerForAll {

  override val containerDef = PostgreSQLContainer.Def(
    dockerImageName = DockerImageName.parse("postgres:15.1"),
    databaseName = "testcontainer-scala",
    username = "scala",
    password = "scala"
  )

  "PostgreSQL container" should "be started" in {
    withContainers { pgContainer =>
      Class.forName(pgContainer.driverClassName)
      val connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sirvildan_db", "postgres", "internet")
      assert(!connection.isClosed())
    }
  }
}