package cn.xdean.webppt.socket;

import org.springframework.web.socket.WebSocketSession;

public interface SocketProvider {

    interface PayloadGetter {
        <T> T get(Class<T> clz);
    }

    interface SocketEventHandler {
        void handleEvent(String event, PayloadGetter payloadGetter);
    }

    String topic();

    SocketEventHandler create(WebSocketSession session);
}
