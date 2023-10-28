package scalasql.dialects

import scalasql.operations.TableOps
import scalasql.query.{
  Aggregatable,
  Expr,
  InsertReturning,
  InsertSelect,
  InsertValues,
  OnConflict,
  Select
}
import scalasql.renderer.{Context, SqlStr}
import scalasql.utils.OptionPickler
import scalasql.{MappedType, Queryable, Table, operations}

trait Dialect {
  implicit def ExprBooleanOpsConv(v: Expr[Boolean]): operations.ExprBooleanOps =
    new operations.ExprBooleanOps(v)
  implicit def ExprNumericOpsConv[T: Numeric: MappedType](v: Expr[T])
      : operations.ExprNumericOps[T] =
    new operations.ExprNumericOps(v)
  implicit def ExprOpsConv(v: Expr[_]): operations.ExprOps = new operations.ExprOps(v)
  implicit def ExprStringOpsConv(v: Expr[String]): operations.ExprStringOps
  implicit def AggNumericOpsConv[V: Numeric: MappedType](v: Aggregatable[Expr[V]])(implicit
      qr: Queryable[Expr[V], V]
  ): operations.AggNumericOps[V] =
    new operations.AggNumericOps(v)

  implicit def AggOpsConv[T](v: Aggregatable[T])(implicit
      qr: Queryable[T, _]
  ): operations.AggOps[T] =
    new operations.AggOps(v)

  implicit def SelectOpsConv[T](v: Select[T, _]): operations.SelectOps[T] =
    new operations.SelectOps(v)

  implicit def TableOpsConv[V[_[_]]](t: Table[V]): TableOps[V] = new TableOps(t)
}
