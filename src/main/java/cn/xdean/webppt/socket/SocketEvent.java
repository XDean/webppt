package cn.xdean.webppt.socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class SocketEvent {
    String topic;

    String event;

    JsonNode payload;
}
