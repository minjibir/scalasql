# ScalaSql

ScalaSql is a Scala library that allows type-safe low-boilerplate querying of
SQL databases, using "standard" Scala collections operations running against
typed `Table` descriptions.

```scala
case class City[+T[_]](
    id: T[Int],
    name: T[String],
    countryCode: T[String],
    district: T[String],
    population: T[Long]
)

object City extends Table[City]() {
  val metadata = initMetadata()
}

val db: DbApi = ???

// Adding up population of all cities in China
val query = City.select.filter(_.countryCode === "CHN").map(_.population).sum
db.toSqlQuery(query) ==>
  "SELECT SUM(city0.population) AS res FROM city city0 WHERE city0.countrycode = ?"

db.run(query) ==> 175953614

// Finding the 5-10th largest cities by population
val query = City.select
  .sortBy(_.population).desc
  .drop(5).take(5)
  .map(c => (c.name, c.population))

db.toSqlQuery(query) ==> """
SELECT city0.name AS res__0, city0.population AS res__1
FROM city city0
ORDER BY res__1 DESC
LIMIT ? OFFSET ?
"""

db.run(query) ==> Seq(
  ("Karachi", 9269265),
  ("Istanbul", 8787958),
  ("Ciudad de México", 8591309),
  ("Moscow", 8389200),
  ("New York", 8008278)
)
```

ScalaSql supports PostgreSQL, MySQL, Sqlite, and H2 databases. Support for additional 
databases can be easily added.

ScalaSql is a relatively new library, so please try it out, but be aware you may hit bugs
or missing features! Please open [Discussions](https://github.com/com-lihaoyi/scalasql/discussions)
for any questions, file [Issues](https://github.com/com-lihaoyi/scalasql/issues) for any 
bugs you hit, or send [Pull Requests](https://github.com/com-lihaoyi/scalasql/pulls) if
you are able to investigate and fix them!


## Getting Started

To get started with ScalaSql, add it to your `build.sc` file as follows:

```scala
ivy"com.lihaoyi::scalasql:0.1.0"
```

## Documentation

* ScalaSql Quickstart Examples: self-contained files showing how to set up ScalaSql with
  a variety of supported databases and perform simple DDL and 
  `SELECT`/`INSERT`/`UPDATE`/`DELETE` operations:
    * [Postgres](scalasql/test/src/example/PostgresExample.scala)
    * [MySql](scalasql/test/src/example/MySqlExample.scala)
    * [Sqlite](scalasql/test/src/example/SqliteExample.scala)
    * [H2](scalasql/test/src/example/H2Example.scala)
    * [HikariCP](scalasql/test/src/example/HikariCpExample.scala) (and other connection pools)

* [ScalaSql Tutorial](docs/tutorial.md): a structured walkthrough of how to use ScalaSql,
  connecting to a database and writing queries to `SELECT`/`INSERT`/`UPDATE`/`DELETE`
  against it to perform useful work. Ideal for newcomers to work through from top
  to bottom when getting started with the library.

* [ScalaSql Reference](docs/reference.md): a detailed listing of ScalaSql functionality,
  comprehensively covering everything that ScalaSql supports, in a single easily searchable
  place. Ideal for looking up exactly methods/operators ScalaSql supports, looking up
  how ScalaSql code translates to SQL, or looking up SQL syntax to find out how to
  express it using ScalaSql. Useful subsections include:
  * [DbApi](docs/reference.md#dbapi), covering the main methods you can all
    to execute queries
  * [Transaction](docs/reference.md#transaction), covering usage of transactions
    and savepoints
  * [Select](docs/reference.md#select), [Insert](docs/reference.md#insert), 
    [Update](docs/reference.md#update), [Delete](docs/reference.md#delete):
    covering operations on the primary queries you are likely to use
  * [Join](docs/reference.md#join), covering different kinds of joins
  * [Returning](docs/reference.md#returning), [On Conflict](docs/reference.md#onconflict):
    covering these modifiers on `INSERT` and `UPDATE` for the databases that support them
  * [Expression Ops](docs/reference.md#exprops), covering the different
    types of `Expr[T]` values and the different operations you can do on each one
  * [Option Ops](docs/reference.md#optional), operations on `Expr[Option[T]`
  * [Window Functions](docs/reference.md#windowfunctions), 
    [With-Clauses/Common-Table-Expressions](docs/reference.md#withcte)
  * [Postgres](docs/reference.md#postgresdialect), [MySql](docs/reference.md#mysqldialect),
    [Sqlite](docs/reference.md#sqlitedialect), [H2](docs/reference.md#h2dialect) Dialects:
    operations that are specific to each database that may not be generally applicable

* [ScalaSql Design](docs/design.md): discusses the design of the ScalaSql library, why it
  is built the way it is, what tradeoffs it makes, and how it compares to other 
  common Scala database query libraries. Ideal for contributors who want to understand
  the structure of the ScalaSql codebase, or for advanced users who may need to
  understand enough to extend ScalaSql with custom functionality.

## Developer Docs

* Running all unit tests: `./mill -i -w "__.test"`
* Running all unit tests on one database: `./mill -i -w "__.test scalasql.sqlite"`
* Re-generating docs: `./mill -i "__.test" + generateTutorial + generateReference`
  * Note that ScalaSql's reference docs are extracted from the test suite, and thus we need
    to make sure to run the test suite before re-generating them.
* Fix all auto-generating and auto-formatting issues at once via
```
./mill -i -w  mill.scalalib.scalafmt.ScalafmtModule/reformatAll __.sources + "scalasql[2.13.12].test" + generateTutorial + generateReference
```

## Changelog

### 0.1.0

* First release!

# TODO

* Scala 3 support

