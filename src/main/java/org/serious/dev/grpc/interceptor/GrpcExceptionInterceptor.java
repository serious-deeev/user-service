package org.serious.dev.grpc.interceptor;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.serious.dev.exception.NoSuchUserException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.serious.dev.grpc.interceptor.GrpcRequestIdInterceptor.REQUEST_ID_KEY;

@Slf4j
@Order(100)
@Component
public class GrpcExceptionInterceptor implements ServerInterceptor {

    private static final Map<Class<? extends Exception>, Status> EXCEPTION_STATUS_MAP = Map.of(
            NoSuchUserException.class, Status.NOT_FOUND
    );

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        String requestId = REQUEST_ID_KEY.get();
        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

        return getServerCallListener(call, delegate, requestId);
    }

    private <ReqT, RespT> ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> getServerCallListener(
            ServerCall<ReqT, RespT> call,
            ServerCall.Listener<ReqT> delegate,
            String requestId
    ) {
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception exception) {
                    logError(requestId, exception);
                    handleEndpointException(exception, call);
                }
            }
        };
    }

    private void logError(String requestId, Exception exception) {
        log.error("[{}] при обработке запроса возникла ошибка: {}",
                requestId,
                exception.getMessage());
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
