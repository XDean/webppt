package cn.xdean.webppt.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketController extends TextWebSocketHandler {

    @Autowired ObjectMapper objectMapper;
    @Autowired SocketProvider socketProviders;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        SocketEvent e = (SocketEvent) objectMapper.readerFor(SocketEvent.class).readValue(message.asBytes());
    }
}
