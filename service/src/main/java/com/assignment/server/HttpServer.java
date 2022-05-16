package com.assignment.server;

import com.assignment.service.UserAccountService;
import com.assignment.util.Constants;
import com.twitter.finagle.Http;
import com.twitter.finagle.ListeningServer;

public class HttpServer {

    public static ListeningServer startServer() {
        var server = Http.serve(":" + Constants.HTTP_SERVER_PORT, new UserAccountService());
        System.out.println("http Server started. Listening on port:"+ Constants.HTTP_SERVER_PORT);
        return server;
    }
}
