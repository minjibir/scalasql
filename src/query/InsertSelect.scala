package usql.query

import renderer.InsertToSql
import usql.renderer.{Context, SqlStr}
import usql.Queryable
import usql.utils.OptionPickler

/**
 * Syntax reference
 *
 * https://www.postgresql.org/docs/current/sql-update.html
 */
case class InsertSelect[Q, C](insert: Insert[Q], columns: C, select: Select[C])(implicit
    val qr: Queryable[Q, _],
    qrc: Queryable[C, _]
) extends Returnable[Q] {
  def expr = insert.expr
  def table = insert.table

  override def toSqlQuery(implicit ctx: Context): SqlStr =
    InsertSelect.InsertSelectQueryable[Q, C](qrc).toSqlQuery(this, ctx)
}

object InsertSelect {

  implicit def InsertSelectQueryable[Q, C](implicit
      qr: Queryable[C, _]
  ): Queryable[InsertSelect[Q, C], Int] =
    new InsertSelectQueryable[Q, C]()(qr)

  class InsertSelectQueryable[Q, C](implicit qr: Queryable[C, _])
      extends Queryable[InsertSelect[Q, C], Int] {
    override def isExecuteUpdate = true
    def walk(ur: InsertSelect[Q, C]): Seq[(List[String], Expr[_])] = Nil

    override def singleRow = true

    def valueReader: OptionPickler.Reader[Int] = OptionPickler.IntReader

    override def toSqlQuery(q: InsertSelect[Q, C], ctx0: Context): SqlStr = {
      InsertToSql.select(
        q,
        qr.walk(q.columns).map(_._2),
        ctx0.tableNameMapper,
        ctx0.columnNameMapper
      )
    }
  }
}
