package cn.xdean.webppt.core.render.element;

import cn.xdean.webppt.core.model.Element;
import cn.xdean.webppt.core.render.RenderContext;
import lombok.Builder;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PageRender extends TemplateElementRender {

    @Autowired FreeMarkerViewResolver viewResolver;

    public PageRender() {
        super("page");
    }

    @Override
    protected void preRenderCheck(Element element) {
        assertTopLevel(element);
    }

    @Override
    protected PageModel generateModel(RenderContext ctx, Element element) {
        List<Element> pages = Objects.requireNonNull(element.getParent()).getElements().stream()
                .filter(e -> this.support(e.getName()))
                .collect(Collectors.toList());
        return PageModel.builder()
                .pageCount(pages.size())
                .page(pages.indexOf(element))
                .build();
    }

    @Override
    public void initContext(RenderContext context) {
        context.scripts.add("/static/js/page.js");
        context.styles.add("/static/webjars/fontawesome/4.7.0/css/font-awesome.css");
        context.styles.add("/static/css/page.css");
        context.globalElements.add(renderService.renderView("element/page_global.ftlh", Collections.emptyMap()));
    }

    @Value
    @Builder
    public static class PageModel {
        int page;
        int pageCount;
    }
}
