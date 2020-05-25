package cn.xdean.webppt.controller.socket;

import cn.xdean.spring.ex.websocket.topic.*;
import cn.xdean.webppt.core.code.language.CodeLanguage;
import cn.xdean.webppt.core.code.language.CodeLanguageService;
import cn.xdean.webppt.core.code.run.CodeRunner;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

@Component
public class RunCodeSocketTopic implements WebSocketTopic {

    @Autowired CodeLanguageService codeLanguageService;
    @Autowired WebSocketTopicHelper helper;
    @Autowired WebSocketTopicExceptionHandler exceptionHandler;

    @Override
    public String topic() {
        return "code";
    }

    @Override
    public WebSocketTopicEventHandler create(WebSocketSession session) {
        return new EventHandler(session);
    }

    private class EventHandler implements WebSocketTopicEventHandler {
        final WebSocketSession session;
        final Map<Integer, Disposable> runs = new ConcurrentHashMap<>();

        public EventHandler(WebSocketSession session) {
            this.session = session;
        }

        @Override
        public void handleEvent(WebSocketSession session, String event, WebSocketPayloadHolder holder) {
            switch (event) {
                case "run":
                    RunRequest runRequest = uncheck(() -> holder.getPayload(RunRequest.class));
                    Assert.isTrue(runRequest.id != 0, "id not given");
                    Assert.hasLength(runRequest.language, "language not given");
                    Assert.hasLength(runRequest.content, "content not given");
                    Optional<CodeLanguage> lang = codeLanguageService.getLanguageByName(runRequest.language);
                    if (!lang.isPresent()) {
                        throw new IllegalArgumentException("no such language: " + runRequest.language);
                    }
                    CodeRunner runner = lang.get().getRunner();
                    if (runner == null) {
                        throw new IllegalArgumentException("language cannot be run: " + runRequest.language);
                    }
                    if (!runner.isSupport()) {
                        throw new IllegalArgumentException("language is not supported on server: " + runRequest.language);
                    }
                    if (runs.containsKey(runRequest.id)) {
                        throw new IllegalArgumentException("id duplicate");
                    }
                    Disposable d = runner.run(runRequest.content)
                            .subscribeOn(Schedulers.computation())
                            .doFinally(() -> {
                                helper.sendEvent(session, "code", "close", runRequest.id);
                                runs.remove(runRequest.id);
                            })
                            .subscribe(e -> {
                                helper.sendEvent(session, "code", "line", LineResponse.builder()
                                        .id(runRequest.id)
                                        .type(e.getType().toString())
                                        .message(e.getMessage())
                                        .build());
                            }, e -> {
                                if (e instanceof Exception) {
                                    exceptionHandler.handle(session, (Exception) e);
                                }
                            });
                    runs.put(runRequest.id, d);
                    break;
                case "stop":
                    break;
                default:
                    throw new IllegalArgumentException("unknown event type: " + event);
            }
        }
    }

    @Data
    private static class RunRequest {
        int id;

        String language;

        String content;
    }

    @Data
    private static class StopRequest {
        int id;
    }

    @Data
    @Builder
    private static class LineResponse {
        int id;

        String type;

        String message;
    }
}
