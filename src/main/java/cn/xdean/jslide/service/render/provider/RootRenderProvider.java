package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.error.RenderException;
import cn.xdean.jslide.service.render.RenderContext;
import cn.xdean.jslide.service.render.RenderKeys;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

@Component
public class RootRenderProvider extends AbstractRenderProvider {
    public RootRenderProvider() {
        super("root");
    }

    @Override
    public String render(Element element) {
        if (!element.isRoot()) {
            throw RenderException.builder()
                    .index(element.getLineIndex())
                    .message("can't define root element")
                    .build();
        }
        RenderContext context = new RenderContext();

        renderService.getAllProviders(element).stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .forEach(p -> p.initContext(context));

        return renderService.renderView("root.ftlh", getDefaultModelMap(element)
                .addAttribute(RenderKeys.CONTEXT, context));
    }

    @Override
    public void initContext(RenderContext context) {
        context.styles.add("/static/css/root.css");
    }
}
