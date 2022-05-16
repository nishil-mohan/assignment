package com.assignment.util;

import com.twitter.finagle.Http;
import com.twitter.finagle.Service;
import com.twitter.finagle.http.Request;
import com.twitter.finagle.http.Response;

import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    static Map<String, Service<Request, Response>> clientMap = new HashMap();

    public synchronized static Service<Request, Response> getRestClient(String url) {
        Service<Request, Response> service = clientMap.get(url);
        if (service == null) {
            service = Http.newService(url);
            clientMap.put(url, service);
        }
        return service;
    }
}
