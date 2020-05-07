package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.error.JSlideException;
import cn.xdean.jslide.core.render.RenderContext;
import cn.xdean.jslide.core.render.RenderKeys;
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
public class PageRender extends AbstractElementRender {

    @Autowired FreeMarkerViewResolver viewResolver;

    public PageRender() {
        super("page");
    }

    @Override
    public String render(Element element) {
        if (!element.isDeep(1)) {
            throw JSlideException.builder()
                    .line(element.getRawInfo().getStartLineIndex())
                    .message("page must be top level element")
                    .build();
        }
        List<Element> pages = Objects.requireNonNull(element.getParent()).getElements().stream()
                .filter(e -> this.support(e.getName()))
                .collect(Collectors.toList());
        return renderService.renderView("page.ftlh", getDefaultModelMap(element)
                .addAttribute(RenderKeys.MODEL, PageModel.builder()
                        .pageCount(pages.size())
                        .page(pages.indexOf(element))
                        .build()));
    }

    @Override
    public void initContext(RenderContext context) {
        context.scripts.add("/static/js/page.js");
        context.styles.add("/static/webjars/fontawesome/4.7.0/css/font-awesome.css");
        context.styles.add("/static/css/page.css");
        context.globalElements.add(renderService.renderView("page_global.ftlh", Collections.emptyMap()));
    }

    @Value
    @Builder
    public static class PageModel {
        int page;
        int pageCount;
    }
}
