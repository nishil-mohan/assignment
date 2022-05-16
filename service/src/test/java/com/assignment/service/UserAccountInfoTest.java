package com.assignment.service;


import com.assignment.dto.AccountInfo;
import com.assignment.dto.LoginInfo;
import com.assignment.dto.UserAccountInfo;
import com.assignment.dto.UserInfo;
import com.assignment.server.GrpcServer;
import com.assignment.server.HttpServer;
import com.assignment.util.Constants;
import com.assignment.util.JsonUtil;
import com.marionete.backends.AccountInfoMock;
import com.marionete.backends.UserInfoMock;
import com.twitter.finagle.Http;
import com.twitter.finagle.ListeningServer;
import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Await;
import com.twitter.util.Future;
import io.grpc.Server;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAccountInfoTest {

    static ListeningServer serverEndpoint = null;
    static Server grpcServer = null;
    static ListeningServer accountMockServer = null;
    static ListeningServer userMockServer = null;

    @BeforeAll
     static void setup() throws Exception {
        System.out.println("starting  all servers..");

        serverEndpoint = HttpServer.startServer();
        grpcServer = GrpcServer.startService();
        accountMockServer = AccountInfoMock.start();
        userMockServer = UserInfoMock.start();
    }

    @AfterAll
     static void cleanup() throws Exception {
        System.out.println("Shutting down all servers..");
        serverEndpoint.close();
        grpcServer.shutdownNow();
        accountMockServer.close();
        userMockServer.close();
    }

    @Test
    public void test_givenServerAndClient_whenUserCredentialSent_thenReceiveAccountResponse() throws Exception {
        // given
        //  All servers initialised as part of setup hook
        var client = Http.newService("localhost:" + Constants.HTTP_SERVER_PORT);
        Request request = Request.apply(Method.Get(), "/marionete/useraccount/");

        // when
        LoginInfo login = new LoginInfo();
        login.setUsername("bla");
        login.setPassword("floo");
        String requestBody = JsonUtil.writeToJson(login);
        request.setContentString(requestBody);

        Future<Response> responseFuture = client.apply(request);

        // then
        var response = Await.result(responseFuture);
        var actualResponse = JsonUtil.parseJson(response.getContentString(), UserAccountInfo.class);
        var expectedResponse = getExpectedResponse();
        System.out.println("Actual Response -" + actualResponse);
        assertEquals(actualResponse, expectedResponse, "Response not matching");

    }

    @Test
    public void test_givenServerAndClient_whenMissingUserCredential_thenReceive401() throws Exception {
        // given
        //  All servers initialised as part of setup hook
        var client = Http.newService("localhost:" + Constants.HTTP_SERVER_PORT);
        Request request = Request.apply(Method.Get(), "/marionete/useraccount/");

        // when
        LoginInfo login = new LoginInfo();
        login.setUsername("bla");
        login.setPassword("");
        String requestBody = JsonUtil.writeToJson(login);
        request.setContentString(requestBody);

        Future<Response> responseFuture = client.apply(request);

        // then
        var response = Await.result(responseFuture);
        assertEquals(401, response.statusCode());

    }

    private UserAccountInfo getExpectedResponse() {
        UserAccountInfo response = new UserAccountInfo();
        UserInfo user = new UserInfo();
        user.setName("John");
        user.setSurname("Doe");
        user.setAge(32);
        user.setSex("male");
        AccountInfo account = new AccountInfo();
        account.setAccountNumber("12345-3346-3335-4456");
        response.setAccountInfo(account);
        response.setUserInfo(user);
        return response;
    }


}
