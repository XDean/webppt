package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.service.render.RenderProvider;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BoldRenderProvider implements RenderProvider {
    @Override
    public boolean support(String name) {
        return name.equals("bold");
    }

    @Override
    public String render(Element element) {
        return String.format("<b>%s</b>", element.getChildren()
                .stream()
                .map(e -> e.asRight().orElse(""))
                .collect(Collectors.joining("<br/>")));
    }
}
