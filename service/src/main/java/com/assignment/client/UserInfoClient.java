package com.assignment.client;

import com.assignment.util.HttpClient;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Method;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;
import com.twitter.util.Future;

public class UserInfoClient {
    public static Future getUserInformation(String token) {
        Service<Request, Response> httpClient = HttpClient.getRestClient("localhost:8898");
        Request request = Request.apply(Method.Get(), "/marionete/user/");
        request.headerMap().put("Authorization", token);
        Future account = httpClient.apply(request);
        return account;
    }


}
