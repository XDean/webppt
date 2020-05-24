package cn.xdean.webppt.core.render.element;

import cn.xdean.webppt.core.model.Element;
import cn.xdean.webppt.core.error.AppException;
import cn.xdean.webppt.core.render.RenderContext;
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
            throw AppException.builder()
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

        context.scripts.add("/static/webjars/jquery/3.5.1/jquery.min.js");

        context.scripts.add("/static/webjars/jquery-ui/1.12.1/jquery-ui.min.js");
        context.styles.add("/static/webjars/jquery-ui/1.12.1/jquery-ui.min.css");
    }

    @Value
    @Builder
    public static class RootModel {
        RenderContext context;
        String title;
    }
}
