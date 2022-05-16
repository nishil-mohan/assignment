package com.assignment.service;

import services.LoginResponse;
import services.LoginServiceGrpc;


public class AuthService extends LoginServiceGrpc.LoginServiceImplBase {

    @Override
    public void login(services.LoginRequest request,
                      io.grpc.stub.StreamObserver<services.LoginResponse> responseObserver) {
        if (request.getUsernameBytes().isEmpty() || request.getPasswordBytes().isEmpty()) {
            responseObserver.onError(new IllegalArgumentException("Invalid login request"));
        } else {
            LoginResponse loginResponse = LoginResponse.newBuilder().setToken("SampleToken").build();
            responseObserver.onNext(loginResponse);
            responseObserver.onCompleted();
        }

    }

}
