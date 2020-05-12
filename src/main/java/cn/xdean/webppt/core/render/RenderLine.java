package cn.xdean.webppt.core.render;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RenderLine {
    boolean isElement; // or text line

    String content;

    public static RenderLine element(String s) {
        return RenderLine.builder().isElement(true).content(s).build();
    }

    public static RenderLine line(String s) {
        return RenderLine.builder().isElement(false).content(s).build();
    }
}
