package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.service.render.RenderContext;
import cn.xdean.jslide.service.render.RenderLine;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.stereotype.Component;
import xdean.jex.extra.collection.Either;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarkdownRender extends AbstractRender {

    Parser parser;
    HtmlRenderer renderer;

    public MarkdownRender() {
        super("md", "markdown");

        MutableDataHolder options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);

        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    @Override
    public String render(Element element) {
        return renderService.renderView("markdown.ftlh", getDefaultModelMap(element));
    }

    @Override
    public void initContext(RenderContext context) {
        context.styles.add("/static/css/md.css");
    }

    @Override
    protected List<RenderLine> processChildren(Element element) {
        List<RenderLine> res = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (Either<Element, String> child : element.getChildren()) {
            if (child.isLeft()) {
                if (!lines.isEmpty()) {
                    res.add(RenderLine.element(processMarkdown(lines)));
                    lines.clear();
                }
                res.add(RenderLine.element(renderService.renderElement(child.getLeft())));
            } else {
                lines.add(child.getRight());
            }
        }
        if (!lines.isEmpty()) {
            res.add(RenderLine.element(processMarkdown(lines)));
            lines.clear();
        }
        return res;
    }

    private String processMarkdown(List<String> lines) {
        String content = String.join("\n", lines);
        return renderer.render(parser.parse(content));
    }
}
