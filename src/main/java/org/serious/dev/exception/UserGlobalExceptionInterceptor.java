package org.serious.dev.exception;

import io.grpc.ForwardingServerCall;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.serious.dev.grpc.UserRequest;
import org.serious.dev.grpc.UserResponse;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.Map;
import java.util.UUID;

import static io.grpc.Grpc.TRANSPORT_ATTR_REMOTE_ADDR;

@Slf4j
@Component
public class UserGlobalExceptionInterceptor implements ServerInterceptor {

    private static final Metadata.Key<String> X_REQUEST_ID_HEADER =
            Metadata.Key.of("x-request-id", Metadata.ASCII_STRING_MARSHALLER);

    private static final Map<Class<? extends Exception>, Status> EXCEPTION_STATUS_MAP = Map.of(
            NoSuchUserException.class, Status.NOT_FOUND
    );

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        String requestId = getRequestId(headers);
        SocketAddress clientAddress = call.getAttributes().get(TRANSPORT_ATTR_REMOTE_ADDR);
        String incomingRpc = call.getMethodDescriptor().getFullMethodName();

        ServerCall<ReqT, RespT> serverCall = new ForwardingServerCall.SimpleForwardingServerCall<>(call) {
            @Override
            public void sendMessage(RespT responseMessage) {
                logResponse(clientAddress, requestId, responseMessage);
                super.sendMessage(responseMessage);
            }
        };

        ServerCall.Listener<ReqT> delegate = next.startCall(serverCall, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onMessage(ReqT requestMessage) {
                logRequest(clientAddress, incomingRpc, requestId, requestMessage);
                super.onMessage(requestMessage);
            }

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

    private <RespT, ReqT> void handleEndpointException(Exception exception, ServerCall<ReqT, RespT> call) {
        Status actualStatus = EXCEPTION_STATUS_MAP.getOrDefault(exception.getClass(), Status.INTERNAL);
        call.close(
                actualStatus
                        .withCause(exception)
                        .withDescription(exception.getMessage()),
                new Metadata()
        );
    }

    private String getRequestId(Metadata headers) {
        String requestId = headers.get(X_REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }

    private <ReqT> void logRequest(SocketAddress clientAddress, String incomingRpc, String requestId, ReqT requestMessage) {
        UserRequest request = (UserRequest) requestMessage;
        log.info("[{}] получен grpc-запрос → client: {}, rpcCall: {}, id пользователя: {}",
                requestId,
                clientAddress,
                incomingRpc,
                request.getId());
    }

    private <RespT> void logResponse(SocketAddress clientAddress, String requestId, RespT responseMessage) {
        UserResponse response = (UserResponse) responseMessage;
        log.info("[{}] найден пользователь с id = {}, отправляем ответ client: {}",
                requestId,
                response.getId(),
                clientAddress);
    }

    private void logError(String requestId, Exception exception) {
        log.error("[{}] при обработке запроса возникла ошибка: {}",
                requestId,
                exception.getMessage());
    }
}
