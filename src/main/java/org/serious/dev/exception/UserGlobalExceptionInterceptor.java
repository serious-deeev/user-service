package org.serious.dev.exception;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

import java.util.Map;

public class UserGlobalExceptionInterceptor implements ServerInterceptor {

    private static final Map<Class<? extends Exception>, Status> EXCEPTION_STATUS_MAP = Map.of(
            NoSuchUserException.class, Status.NOT_FOUND
    );

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {

        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception exception) {
                    handleEndpointException(exception, call);
                }
            }
        };
    }

    private <RespT, ReqT> void handleEndpointException(Exception exception, ServerCall<ReqT, RespT> call) {
        Status actualStatus = EXCEPTION_STATUS_MAP.getOrDefault(exception.getClass(), Status.INTERNAL);
        call.close(
                actualStatus
                        .withCause(exception)
                        .withDescription(exception.getMessage()),
                new Metadata()
        );
    }
}
