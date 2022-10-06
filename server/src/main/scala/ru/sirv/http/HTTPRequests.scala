package ru.sirv.http

object HTTPRequests extends App{
  val r = requests.get(
    "https://api.github.com/repos/sirvildan/user_manager/issues/1")
  //val r = requests.get("http://localhost:8000/test")
  println(r.text)
}