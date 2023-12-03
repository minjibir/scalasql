package scalasql.utils

import scalasql.core.{Config, Sql, SqlStr}
import scalasql.core.SqlStr.Renderable
import scalasql.renderer.Context

/**
 * Converts back and forth between a tree-shaped JSON and flat key-value map
 */
object FlatJson {
  def flatten(tokens: List[String], context: Context) = {
    (context.config.columnLabelPrefix +: tokens).mkString(context.config.columnLabelDelimiter)
  }

  def flatten(x: Seq[(List[String], Sql[_])], context: Context): Seq[(String, SqlStr)] = {
    x.map { case (k, v) => (flatten(k, context), Renderable.renderToSql(v)(context)) }
  }

}
