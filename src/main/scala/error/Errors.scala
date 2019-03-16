package error

sealed trait CError extends Throwable

case object EmptyInput extends CError

sealed trait ParseError extends CError

case class PilotParseError(input: String) extends ParseError

case class LapParseError(input: String) extends ParseError

case class TimeParserError(input: String) extends ParseError
