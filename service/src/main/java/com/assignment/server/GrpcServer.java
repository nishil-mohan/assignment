package com.assignment.server;

import com.assignment.service.AuthService;
import com.assignment.util.Constants;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    public static Server startService() throws IOException, InterruptedException {
        Server server = ServerBuilder
                .forPort(Constants.GRPC_SERVER_PORT)
                .addService(new AuthService())
                .build();

        server.start();
        System.out.println("gRPC Server started, Listening on port:" + server.getPort());
        return server;
    }



}
