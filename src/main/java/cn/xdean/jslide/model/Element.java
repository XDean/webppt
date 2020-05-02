package cn.xdean.jslide.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class Element {
    String name;

    @Singular
    Map<String, String> parameters;

    @Singular
    List<Element> elements;

    @Singular
    List<String> lines;
}
