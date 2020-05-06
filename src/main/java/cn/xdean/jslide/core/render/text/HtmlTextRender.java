package cn.xdean.jslide.core.render.text;

import cn.xdean.jslide.core.model.Element;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HtmlTextRender extends AbstractTextRender {
    public HtmlTextRender() {
        super("html");
    }

    @Override
    public String render(Element element, List<String> lines) {
        return String.join("\n", lines);
    }
}
