package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.service.render.RenderContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
public class MarkdownRenderProvider extends AbstractRenderProvider {



    public MarkdownRenderProvider() {
        super("md", "markdown");
    }

    @Override
    public String render(Element element) {
        return null;
    }

    @Override
    public void initContext(RenderContext context) {

    }
}
