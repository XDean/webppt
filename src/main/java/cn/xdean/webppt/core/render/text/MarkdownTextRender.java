package cn.xdean.webppt.core.render.text;

import cn.xdean.webppt.core.model.Text;
import cn.xdean.webppt.core.render.RenderContext;
import cn.xdean.webppt.core.render.RenderKeys;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.Builder;
import lombok.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

@Component
@Primary
public class MarkdownTextRender extends AbstractTextRender {
    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownTextRender() {
        super("md", "markdown");
        MutableDataHolder options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);

        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    @Override
    public String render(Text text) {
        String content = String.join("\n", text.getLines());
        return renderService.renderView("text/md.ftlh", new ModelMap()
                .addAttribute(RenderKeys.MODEL, MarkdownTextModel.builder().content(renderer.render(parser.parse(content))).build()));
    }

    @Override
    public void initContext(RenderContext context) {
        context.styles.add("/static/css/md.css");
    }

    @Value
    @Builder
    public static class MarkdownTextModel {
        String content;
    }
}
