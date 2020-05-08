package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.error.JSlideException;
import cn.xdean.jslide.core.render.RenderContext;
import cn.xdean.jslide.core.render.RenderKeys;
import lombok.Builder;
import lombok.Value;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

@Component
public class RootRender extends TemplateElementRender {
    public RootRender() {
        super("root");
    }

    @Override
    protected void preRenderCheck(Element element) {
        if (!element.isRoot()) {
            throw JSlideException.builder()
                    .line(element.getRawInfo().getStartLineIndex())
                    .message("can't define root element")
                    .build();
        }
    }

    @Override
    protected RootModel generateModel(RenderContext context, Element element) {
        renderService.getElementRenders(element).stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .forEach(p -> p.initContext(context));
        renderService.getTextRenders(element).stream()
                .sorted(AnnotationAwareOrderComparator.INSTANCE)
                .forEach(p -> p.initContext(context));
        return RootModel.builder()
                .context(context)
                .title(resolveParameter(element, "title", null))
                .build();
    }

    @Override
    public void initContext(RenderContext context) {
        context.styles.add("/static/css/root.css");
    }

    @Value
    @Builder
    public static class RootModel {
        RenderContext context;
        String title;
    }
}
