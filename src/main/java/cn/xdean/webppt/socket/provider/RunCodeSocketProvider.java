package cn.xdean.webppt.socket.provider;

import cn.xdean.webppt.core.code.language.CodeLanguageService;
import cn.xdean.webppt.socket.SocketProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class RunCodeSocketProvider implements SocketProvider {

    @Autowired CodeLanguageService codeLanguageService;
    @Autowired ObjectMapper objectMapper;

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
            RunRequest runRequest = payloadGetter.get(RunRequest.class);
            codeLanguageService.getLanguageByName(runRequest.language)
                    .ifPresent(language -> {
                        language.getRunner();
                    });
        }
    }

    @Data
    private static class RunRequest {
        int id;

        String language;

        String content;
    }

    @Data
    private static class RunResponse {

    }
}
