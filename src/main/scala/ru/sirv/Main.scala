package ru.sirv

/*package ru.sirv

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
  }
} */

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}

import java.io.IOException
import java.net.InetSocketAddress


object Test {
  @throws[Exception]
  def main(args: Array[String]): Unit = {
    val server = HttpServer.create(new InetSocketAddress(8000), 0)
    server.createContext("/test", new Test.MyHandler)
    server.setExecutor(null) // creates a default executor

    server.start()
  }

  class MyHandler extends HttpHandler {
    @throws[IOException]
    override def handle(t: HttpExchange): Unit = {
      val response = "This is the response. Testing"
      t.sendResponseHeaders(200, response.length)
      val os = t.getResponseBody
      os.write(response.getBytes)
      os.close()
    }
  }
}
