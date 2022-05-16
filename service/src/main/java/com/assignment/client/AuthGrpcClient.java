package com.assignment.client;

import com.assignment.util.Constants;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import services.LoginRequest;
import services.LoginResponse;
import services.LoginServiceGrpc;

public class AuthGrpcClient {

    static LoginServiceGrpc.LoginServiceBlockingStub stub = null;

    public static LoginResponse authenticateUser(String userName, String password) {

        return getStub().login(
                LoginRequest.newBuilder()
                        .setUsername(userName)
                        .setPassword(password)
                        .build());
    }

    private static synchronized LoginServiceGrpc.LoginServiceBlockingStub getStub() {
        if (stub == null) {
            ManagedChannel channel = ManagedChannelBuilder
                    .forAddress("localhost", Constants.GRPC_SERVER_PORT)
                    .usePlaintext()
                    .build();
            stub = LoginServiceGrpc.newBlockingStub(channel);
        }
        return stub;
    }
}
