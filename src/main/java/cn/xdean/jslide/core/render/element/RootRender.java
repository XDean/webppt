package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.error.JSlideException;
import cn.xdean.jslide.core.render.RenderContext;
import cn.xdean.jslide.core.render.RenderKeys;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

@Component
public class RootRender extends AbstractElementRender {
    public RootRender() {
        super("root");
    }

    @Override
    public String render(Element element) {
        if (!element.isRoot()) {
            throw JSlideException.builder()
                    .line(element.getRawInfo().getStartLineIndex())
                    .message("can't define root element")
                    .build();
        }
        RenderContext context = new RenderContext();

        renderService.getElementRenders(element).stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .forEach(p -> p.initContext(context));
        renderService.getTextRenders(element).stream()
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
