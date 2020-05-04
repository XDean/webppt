package cn.xdean.jslide.core.render;

import cn.xdean.jslide.core.model.Element;

import java.util.List;

public interface TextRender {
    boolean support(String name);

    String render(Element element, List<String> lines);

    default void initContext(RenderContext context) {
        // do nothing
    }
}
