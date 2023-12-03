package scalasql.query

import scalasql.core.{TypeMapper, Queryable, SqlStr, Sql}
import scalasql.renderer.{Context}

/**
 * A reference that aggregations for usage within [[Select.aggregate]], to allow
 * the caller to perform multiple aggregations within a single query.
 */
class SelectProxy[Q](val expr: Q) extends Aggregatable[Q] {
  def queryExpr[V: TypeMapper](f: Q => Context => SqlStr)(
      implicit qr: Queryable.Row[Sql[V], V]
  ): Sql[V] = { Sql[V] { implicit c => f(expr)(c) } }
}
