package com.marionete.backends

import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.finagle.{Http, Service, http}
import com.twitter.util.Await
import org.scalatest.{BeforeAndAfterAll, FeatureSpec, GivenWhenThen}

class AccountInfoMockTest extends FeatureSpec with GivenWhenThen with BeforeAndAfterAll{

  feature("[AccountInfoMock] To ensure that is working as we expect") {

    scenario("[AccountInfoMock] We made a good request with Authentication in header and return result successfully") {
      Given("A [AccountInfoMock] running and a Http client with Authentication header")
      val server = AccountInfoMock.start()
      val client: Service[Request, Response] = Http.newService("localhost:8899")
      When("When I made a request")
      val request = http.Request(http.Method.Get, "/marionete/account/")
      request.headerMap.add("Authorization", "valid_token")
      client(request)
      client(request)
      val future = client(request)
      Then("The Future response is successful")
      val response = Await.result(future)
      assert(response.status == Status.Ok)
      Await.result(server.close())
    }

    scenario("[AccountInfoMock] We made a bad request without Authentication in header and return result Error") {
      Given("A [AccountInfoMock] running and a Http client without Authentication header")
      val server = AccountInfoMock.start()
      val client: Service[Request, Response] = Http.newService("localhost:8899")
      When("When I made a request")
      val request = http.Request(http.Method.Get, "/marionete/account/")
      client(request)
      client(request)
      val future = client(request)
      Then("The Future response is wrong")
      val response = Await.result(future)
      assert(response.status == Status.InternalServerError)
      Await.result(server.close())
    }

    scenario("[AccountInfoMock] We made a bad request with wrong endpoint") {
      Given("A [AccountInfoMock] running and a Http client without Authentication header")
      val server = AccountInfoMock.start()
      val client: Service[Request, Response] = Http.newService("localhost:8899")
      When("When I made a request")
      val request = http.Request(http.Method.Get, "/marionete/foo/")
      client(request)
      client(request)
      val future = client(request)
      Then("The Future response is wrong")
      val response = Await.result(future)
      assert(response.status == Status.NotFound)
      Await.result(server.close())
    }
  }
}
