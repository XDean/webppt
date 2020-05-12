package cn.xdean.webppt.core.render;

import cn.xdean.webppt.core.model.Element;

public interface ElementRender {
    boolean support(String name);

    String render(RenderContext ctx, Element element);

    default void initContext(RenderContext context) {
        // do nothing
    }
}
