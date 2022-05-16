package com.marionete.backends

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Failure, Http, ListeningServer, Service, http}
import com.twitter.util.Future

import scala.collection.mutable


object AccountInfoMock {

  private val statuses: mutable.Queue[http.Status] = mutable.Queue[http.Status](
    http.Status.TooManyRequests,
    http.Status.TooManyRequests,
    http.Status.Ok,
    http.Status.Ok,
    http.Status.Ok,
    http.Status.Ok,
    http.Status.Ok,
    http.Status.Ok,
    http.Status.Ok,
    http.Status.Ok
  )

  def start(): ListeningServer = {
    val port = "8899"
    println(s"Starting AccountInfoMock service in port $port")
    Http.serve(":" + port, service)
  }

  private val service: Service[Request, Response] = (req: http.Request) => {
    req.path match {
      case "/marionete/account/" => processAccountEndpoint(req)
      case _ =>
        val rep = Response(http.Status.NotFound)
        Future.value(rep)
    }
  }

  private def processAccountEndpoint(req: Request): Future[Response] = {
    req.headerMap.get("Authorization") match {
      case Some(token) =>
        val response =
          """
                {
                   "accountNumber":"12345-3346-3335-4456"
                }
            |""".stripMargin
        val status = statuses.dequeue()
        val rep = Response(status)
        if(status == Status.Ok){
          println(s"[AccountInfoMock] Request with $token valid. Returning account info.")
          rep.setContentString(response)
          Future.value(rep)
        }else{
          println(s"[AccountInfoMock] Request with $token invalid.")
          Future.exception(Failure.rejected("Service not available"))
        }

      case None =>
        val rep = Response(http.Status.InternalServerError)
        Future.value(rep)
    }
  }

}
