package org.serious.dev.grpc.interceptor;

import io.grpc.ForwardingServerCall;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.serious.dev.grpc.UserRequest;
import org.serious.dev.grpc.UserResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.serious.dev.grpc.interceptor.GrpcRequestIdInterceptor.REQUEST_ID_KEY;

@Slf4j
@Component
@Order(200)
public class GrpcUserRequestInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        String requestId = REQUEST_ID_KEY.get();
        String incomingRpcMethodName = call.getMethodDescriptor().getFullMethodName();
        ServerCall<ReqT, RespT> serverCall = getServerCall(call, requestId);
        ServerCall.Listener<ReqT> delegate = next.startCall(serverCall, headers);

        return getServerCallListener(delegate, requestId, incomingRpcMethodName);
    }

    private <ReqT, RespT> ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> getServerCall(
            ServerCall<ReqT, RespT> call,
            String requestId
    ) {
        return new ForwardingServerCall.SimpleForwardingServerCall<>(call) {
            @Override
            public void sendMessage(RespT responseMessage) {
                logResponse(responseMessage, requestId);
                super.sendMessage(responseMessage);
            }
        };
    }

    private <ReqT> ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT> getServerCallListener(
            ServerCall.Listener<ReqT> delegate,
            String requestId,
            String incomingRpc
    ) {
        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onMessage(ReqT requestMessage) {
                logRequest(requestMessage, requestId, incomingRpc);
                super.onMessage(requestMessage);
            }
        };
    }

    private <ReqT> void logRequest(ReqT requestMessage, String requestId, String incomingRpc) {
        UserRequest request = (UserRequest) requestMessage;
        log.info(
                "[{}] получен grpc-запрос → rpcCall: {}, id пользователя: {}",
                requestId,
                incomingRpc,
                request.getId()
        );
    }

    private <RespT> void logResponse(RespT responseMessage, String requestId) {
        UserResponse response = (UserResponse) responseMessage;
        log.info(
                "[{}] найден пользователь с id = {}, отправляем ответ",
                requestId,
                response.getId()
        );
    }
}
