package ru.sirv.domain

final case class User(email: String, name: String, age: Option[Int])

object User {
  def apply(email: String, name: String, age: Option[Int]): User = new User(email, name, age)

  def apply(email: String, name: String, age: Int): User = new User(email, name, Option(age))

  def apply(email: String, name: String): User = new User(email, name, Option.empty[Int])

  //def isEmailValid(email:String): Boolean = email contains "@"
  def isEmailValid(email: String): Boolean = email.contains("@")
}