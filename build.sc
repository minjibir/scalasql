
import mill._, scalalib._

object usql extends RootModule with ScalaModule {
  def scalaVersion = "2.13.11"
  def ivyDeps = Agg(
    ivy"com.lihaoyi::mainargs:0.4.0",
    ivy"com.lihaoyi::sourcecode:0.3.1",
    ivy"com.lihaoyi::upickle:3.1.3",
    ivy"com.lihaoyi::os-lib:0.9.1",
    ivy"org.scala-lang:scala-reflect:$scalaVersion",
    ivy"com.github.vertical-blank:sql-formatter:2.0.4",
    ivy"org.apache.logging.log4j:log4j-api:2.20.0",
    ivy"org.apache.logging.log4j:log4j-core:2.20.0",
    ivy"org.apache.logging.log4j:log4j-slf4j-impl:2.20.0",
  )

  object test extends ScalaTests {
    def ivyDeps = Agg(
      ivy"com.lihaoyi::utest:0.7.11",
      ivy"com.h2database:h2:2.2.224",
      ivy"org.hsqldb:hsqldb:2.5.1",
      ivy"org.xerial:sqlite-jdbc:3.43.0.0",
      ivy"org.testcontainers:postgresql:1.19.1",
      ivy"org.postgresql:postgresql:42.6.0",
      ivy"org.testcontainers:mysql:1.19.1",
      ivy"mysql:mysql-connector-java:8.0.33",
    )
    def testFramework = "utest.runner.Framework"
  }
}
