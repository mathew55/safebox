package models


final case class User(
                       id: Int
                     )
final case class Users(trips: Seq[User])

final case class CommandResult(count: Int)
