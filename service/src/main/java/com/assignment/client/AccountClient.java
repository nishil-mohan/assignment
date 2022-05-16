package com.assignment.client;

import com.assignment.util.Constants;
import com.assignment.util.HttpClient;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Future;

public class AccountClient {

    public static Future getAccountInformation(String token) {
        Service<Request, Response> httpClient = HttpClient.getRestClient("localhost:" + Constants.MOCK_SERVER_ACCOUNT_PORT);
        Request request = Request.apply(Method.Get(), "/marionete/account/");
        request.headerMap().put("Authorization", token);
        Future account = httpClient.apply(request);
        return account;
    }
}
