package cn.xdean.webppt.socket.provider;

import cn.xdean.webppt.socket.SocketProvider;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class RunCodeSocketProvider implements SocketProvider {
    @Override
    public String topic() {
        return "code";
    }

    @Override
    public SocketEventHandler create(WebSocketSession session) {
        return new EventHandler(session);
    }

    private class EventHandler implements SocketEventHandler {
        final WebSocketSession session;

        public EventHandler(WebSocketSession session) {
            this.session = session;
        }

        @Override
        public void handleEvent(String event, PayloadGetter payloadGetter) {
            RunEvent runEvent = payloadGetter.get(RunEvent.class);
        }
    }

    @Data
    private static class RunEvent {
        String language;

        String content;
    }
}
