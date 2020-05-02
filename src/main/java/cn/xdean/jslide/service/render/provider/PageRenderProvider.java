package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.error.RenderException;
import cn.xdean.jslide.service.render.RenderService;
import cn.xdean.jslide.service.render.RenderProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageRenderProvider implements RenderProvider {

    @Autowired RenderService renderService;

    @Override
    public boolean support(String name) {
        return name.equals("page");
    }

    @Override
    public String render(Element element) {
        if (!element.isDeep(1)) {
            throw RenderException.builder()
                    .index(element.getLineIndex())
                    .message("page must be top level element")
                    .build();
        }
        return "<page>${children}</page>";
    }
}
