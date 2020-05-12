package cn.xdean.webppt.core.render.text;

import cn.xdean.webppt.core.model.Text;
import org.springframework.stereotype.Component;

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
