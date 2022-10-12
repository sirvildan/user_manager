package ru.sirv.service

import cats.effect.IO
import com.typesafe.scalalogging.Logger
import ru.sirv.domain._

class UserService{
  implicit val logger = Logger("UserService")
  def getUser(userId: Int): IO[User] = {
    logger.info(s"Get user by id $userId")
    IO.delay(User("ss@mal.ru", "mock", None))

  }

  //  def getPopularTweets(): IO[Seq[Tweet]] =
//    Seq(Tweet(1, "first"), Tweet(2, "second"), Tweet(3, "third")).pure[IO]

//  def saveTweet(): IO[Unit] = IO.println("Save tweet test").onError { e => logger.error(e.getMessage).pure[IO] }
}