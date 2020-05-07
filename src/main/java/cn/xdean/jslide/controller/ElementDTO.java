package cn.xdean.jslide.controller;

import cn.xdean.jslide.core.model.*;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class ElementDTO {

    String name;

    @Singular
    List<Object> children;

    RawInfo rawInfo;

    public static ElementDTO from(Element e) {
        return ElementDTO.builder()
                .name(e.getName())
                .rawInfo(e.getRawInfo())
                .children(e.getChildren().stream().map(n -> {
                    if (n instanceof Element) {
                        return ElementDTO.from((Element) n);
                    } else if (n instanceof Parameter) {
                        return ParameterDTO.from((Parameter) n);
                    } else {
                        return TextDTO.from((Text) n);
                    }
                }).collect(Collectors.toList()))
                .build();
    }
}
