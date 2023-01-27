package ru.sirv.domain

import org.scalatest.funsuite.AnyFunSuite

class UserEmailValidationTest extends AnyFunSuite {

  def isEmailValid(email: String): Boolean = email.contains("@")

  test("First Test: An Email should be valid") {
    assert(isEmailValid("johndoe@outlook.com"))
  }
  test("Second Test: An Email should be valid") {
    assert(!isEmailValid("johndoe"))
  }
}
