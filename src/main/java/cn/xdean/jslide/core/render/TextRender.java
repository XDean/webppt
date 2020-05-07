package cn.xdean.jslide.core.render;

import cn.xdean.jslide.core.model.Text;

public interface TextRender {
    boolean support(String name);

    String render(Text text);

    default void initContext(RenderContext context) {
        // do nothing
    }
}
