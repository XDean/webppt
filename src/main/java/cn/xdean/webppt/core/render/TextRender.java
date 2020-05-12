package cn.xdean.webppt.core.render;

import cn.xdean.webppt.core.model.Text;

public interface TextRender {
    boolean support(String name);

    String render(Text text);

    default void initContext(RenderContext context) {
        // do nothing
    }
}
