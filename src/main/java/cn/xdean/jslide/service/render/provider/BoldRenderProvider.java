package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.service.render.RenderProvider;
import org.springframework.stereotype.Component;

@Component
public class BoldRenderProvider implements RenderProvider {
    @Override
    public boolean support(String name) {
        return name.equals("bold");
    }

    @Override
    public String render(Element element) {
        return String.format("<b>%s</b>", String.join("<br/>", element.getLines()));
    }
}
