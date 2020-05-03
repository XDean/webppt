package cn.xdean.jslide.service.render;

import cn.xdean.jslide.model.Element;

public interface Render {
    boolean support(String name);

    String render(Element element);

    default void initContext(RenderContext context) {
        // do nothing
    }
}
