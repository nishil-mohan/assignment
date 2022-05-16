package com.assignment.service;

import com.assignment.client.AccountClient;
import com.assignment.client.AuthGrpcClient;
import com.assignment.client.UserInfoClient;
import com.assignment.dto.AccountInfo;
import com.assignment.dto.LoginInfo;
import com.assignment.dto.UserAccountInfo;
import com.assignment.dto.UserInfo;
import com.assignment.util.JsonUtil;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.finagle.http.Status;
import com.twitter.util.Future;
import services.LoginResponse;

import javax.security.sasl.AuthenticationException;


public class UserAccountService extends Service<Request, Response> {


    @Override
    public Future<Response> apply(Request request) {
        Response response = Response.apply();
        switch (request.path()) {
            case "/marionete/useraccount/":
                LoginInfo loginInfo = JsonUtil.parseJson(request.getContentString(), LoginInfo.class);
                if (loginInfo.getUsername().isBlank() || loginInfo.getPassword().isBlank()){
                    response.setContentString("Invalid Credentials");
                    response.status(Status.Unauthorized());
                    return Future.value(response);
                }
                LoginResponse authResponse =
                        AuthGrpcClient.authenticateUser(loginInfo.getUsername(),
                                loginInfo.getPassword());
                String token = authResponse.getToken();

                Future<Response> accountFuture = AccountClient.getAccountInformation(token);
                Future<Response> userInfoFuture = UserInfoClient.getUserInformation(token);

                return accountFuture.joinWith(userInfoFuture, (a, b) -> {
                    AccountInfo accountJson = JsonUtil.parseJson(a.getContentString(), AccountInfo.class);
                    UserInfo userJson = JsonUtil.parseJson(b.getContentString(), UserInfo.class);
                    UserAccountInfo userAccount = new UserAccountInfo();
                    userAccount.setUserInfo(userJson);
                    userAccount.setAccountInfo(accountJson);
                    response.setContentString(JsonUtil.writeToJson(userAccount));
                    response.setContentTypeJson();
                    return response;
                });
            default:
                return Future.exception(new IllegalArgumentException("Invalid route"));
        }

    }


}
