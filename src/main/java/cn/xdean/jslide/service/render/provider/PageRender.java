package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.error.RenderException;
import cn.xdean.jslide.service.render.RenderContext;
import cn.xdean.jslide.service.render.RenderKeys;
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
public class PageRender extends AbstractRender {

    @Autowired FreeMarkerViewResolver viewResolver;

    public PageRender() {
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
        List<Element> pages = Objects.requireNonNull(element.getParent()).getChildren().stream()
                .map(e -> e.asLeft().orElse(null))
                .filter(e -> e != null)
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
