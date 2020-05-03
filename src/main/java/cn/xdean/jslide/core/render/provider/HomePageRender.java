package cn.xdean.jslide.core.render.provider;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.render.RenderContext;
import cn.xdean.jslide.core.render.RenderKeys;
import lombok.Builder;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
public class HomePageRender extends AbstractRender {
    public HomePageRender() {
        super("homepage");
    }

    @Override
    public String render(Element element) {
        return renderService.renderView("homepage.ftlh", getDefaultModelMap(element)
                .addAttribute(RenderKeys.MODEL, HomePageModel.builder()
                        .title(element.resolveParameter("title"))
                        .subtitle(element.resolveParameter("subtitle"))
                        .date(element.resolveParameter("date"))
                        .author(element.resolveParameter("author"))
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
