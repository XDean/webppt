package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.service.render.RenderContext;
import org.springframework.stereotype.Component;

@Component
public class HomePageRenderProvider extends AbstractRenderProvider {
    public HomePageRenderProvider() {
        super("homepage");
    }

    @Override
    public String render(Element element) {
        return renderService.renderView("homepage.ftlh", getDefaultModelMap(element));
    }

    @Override
    public void initContext(RenderContext context) {
        context.styles.add("/static/css/homepage.css");
    }
}
