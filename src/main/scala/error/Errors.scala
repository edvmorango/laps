package error

sealed trait CError extends Throwable

case object EmptyInput extends CError
case class PilotParseError(input: String) extends CError
