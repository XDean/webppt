package cn.xdean.jslide.core.render;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.error.ParseException;
import cn.xdean.jslide.core.error.RenderException;
import cn.xdean.jslide.core.render.element.RootRender;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

@Service
public class RenderService {
    @Autowired List<ElementRender> elementRenders;
    @Autowired List<TextRender> textRenders;
    @Autowired TextRender primaryTextRender;
    @Autowired Configuration freemarkerConfiguration;
    @Autowired RootRender rootRenderProvider;

    public ElementRender getElementRender(Element element) {
        for (ElementRender render : elementRenders) {
            if (render.support(element.getName())) {
                return render;
            }
        }
        throw ParseException.builder().message("Can't render element: " + element.getName()).build();
    }

    public Collection<ElementRender> getElementRenders(Element element) {
        Set<ElementRender> renders = new HashSet<>();
        renders.add(getElementRender(element));
        element.getChildren().forEach(c -> c.ifLeft(e -> renders.addAll(getElementRenders(e))));
        return renders;
    }

    public String renderElement(Element element) {
        return getElementRender(element).render(element);
    }

    public TextRender getTextRender(Element element) {
        String type = element.resolveParameter(RenderKeys.TEXT_TYPE);
        if (type == null) {
            return primaryTextRender;
        }
        for (TextRender render : textRenders) {
            if (render.support(type)) {
                return render;
            }
        }
        throw ParseException.builder().message("Can't render text type: " + type).build();
    }

    public Collection<TextRender> getTextRenders(Element element) {
        Set<TextRender> renders = new HashSet<>();
        renders.add(getTextRender(element));
        element.getChildren().forEach(c -> c.ifLeft(e -> renders.addAll(getTextRenders(e))));
        return renders;
    }

    public String renderText(Element parent, List<String> lines) {
        return getTextRender(parent).render(parent, lines);
    }

    public String renderView(String template, Map<String, Object> model) {
        try {
            Template t = freemarkerConfiguration.getTemplate(template);
            StringWriter out = new StringWriter();
            t.process(model, out);
            return out.toString();
        } catch (TemplateException | IOException e) {
            throw RenderException.builder()
                    .message("Fail to render view: " + template)
                    .cause(e)
                    .build();
        }
    }
}
