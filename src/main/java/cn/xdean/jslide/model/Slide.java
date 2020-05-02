package cn.xdean.jslide.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
public class Slide {
    @Singular
    Map<String, String> parameters;

    @Singular
    List<Element> elements;
}
