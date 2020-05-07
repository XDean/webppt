package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.render.RenderContext;
import cn.xdean.jslide.core.render.RenderKeys;
import lombok.Builder;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
public class HomePageRender extends AbstractElementRender {
    public HomePageRender() {
        super("homepage");
    }

    @Override
    public String render(Element element) {
        return renderService.renderView("homepage.ftlh", getDefaultModelMap(element)
                .addAttribute(RenderKeys.MODEL, HomePageModel.builder()
                        .title(resolveParameter(element, "title", null))
                        .subtitle(resolveParameter(element, "subtitle", null))
                        .date(resolveParameter(element, "date", null))
                        .author(resolveParameter(element, "author", null))
                        .build()));
    }

    @Override
    public void initContext(RenderContext context) {
        context.styles.add("/static/css/homepage.css");
    }

    @Value
    @Builder
    public static class HomePageModel {
        String title;
        String subtitle;
        String date;
        String author;
    }
}
