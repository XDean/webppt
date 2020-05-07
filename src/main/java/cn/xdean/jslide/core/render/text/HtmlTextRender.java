package cn.xdean.jslide.core.render.text;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.model.Text;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HtmlTextRender extends AbstractTextRender {
    public HtmlTextRender() {
        super("html");
    }

    @Override
    public String render(Text text) {
        return String.join("\n", text.getLines());
    }
}
