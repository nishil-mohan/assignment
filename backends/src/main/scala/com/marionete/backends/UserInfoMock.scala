package com.marionete.backends

import com.twitter.finagle.{Failure, Http, ListeningServer, Service, http}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Future}

object UserInfoMock {

  def start(): ListeningServer = {
    val port = "8898"
    println(s"Starting UserAccountMock service in port $port")
    Http.serve(":" + port, service)
  }

  val service: Service[Request, Response] = (req: http.Request) => {
    req.path match {
      case "/marionete/user/" => processUserEndpoint(req)
      case _ =>
        val rep = Response(com.twitter.finagle.http.Status.NotFound)
        Future.value(rep)
    }
  }

  private def processUserEndpoint(req: Request): Future[Response] = {
    req.headerMap.get("Authorization") match {
      case Some(token) =>
        println(s"[UserAccountMock] Request with $token valid. Returning account info.")
        val response =
          """
            {
                "name":"John",
                "surname":"Doe",
                "sex":"male",
                 "age": 32
             }
            |""".stripMargin
        val rep = Response(com.twitter.finagle.http.Status.Ok)
        rep.setContentString(response)
        Future.value(rep)

      case None =>
        val rep = Response(com.twitter.finagle.http.Status.InternalServerError)
        Future.value(rep)
    }
  }

}
