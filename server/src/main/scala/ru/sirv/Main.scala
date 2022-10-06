package ru.sirv

import java.io.IOException
import java.net.InetSocketAddress
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer


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
      val response = "This is the response"
      t.sendResponseHeaders(200, response.length())
      val os = t.getResponseBody
      os.write(response.getBytes)
      os.close()
    }
  }
}