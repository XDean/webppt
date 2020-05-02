package cn.xdean.jslide.service.render;

import cn.xdean.jslide.model.Element;

public interface RenderProvider {

    boolean support(String name);

    String render(Element element);

}
