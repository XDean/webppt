package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.error.RenderException;
import cn.xdean.jslide.service.render.RenderContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Component
public class PageRenderProvider extends AbstractRenderProvider {

    @Autowired FreeMarkerViewResolver viewResolver;

    public PageRenderProvider() {
        super("page");
    }

    @Override
    public String render(Element element) {
        if (!element.isDeep(1)) {
            throw RenderException.builder()
                    .index(element.getLineIndex())
                    .message("page must be top level element")
                    .build();
        }
        return renderService.renderView("page.ftlh", getDefaultModelMap(element));
    }

    @Override
    protected void actualInitContext(RenderContext context, Element element) {
        context.scripts.add("/static/js/page.js");
        context.styles.add("/static/css/page.css");
    }
}
