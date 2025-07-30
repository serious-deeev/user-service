package org.serious.dev.grpc.interceptor;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Order(300)
public class GrpcRequestIdInterceptor implements ServerInterceptor {

    public static final Context.Key<String> REQUEST_ID_KEY = Context.key("requestId");

    private static final Metadata.Key<String> X_REQUEST_ID_HEADER =
            Metadata.Key.of("x-request-id", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        Context contextWithRequestId = Context.current()
                .withValue(REQUEST_ID_KEY, getRequestId(headers));

        return Contexts.interceptCall(contextWithRequestId, call, headers, next);
    }

    private String getRequestId(Metadata headers) {
        String requestId = headers.get(X_REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }
}
