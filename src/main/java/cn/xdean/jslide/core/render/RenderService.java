package cn.xdean.jslide.core.render;

import cn.xdean.jslide.core.error.JSlideException;
import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.error.JSlideException;
import cn.xdean.jslide.core.model.Parameter;
import cn.xdean.jslide.core.model.Text;
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
        throw JSlideException.builder().message("Can't render element: " + element.getName()).build();
    }

    public Collection<ElementRender> getElementRenders(Element element) {
        Set<ElementRender> renders = new HashSet<>();
        renders.add(getElementRender(element));
        element.getChildren()
                .stream()
                .filter(n -> n instanceof Element)
                .forEach(e -> renders.addAll(getElementRenders((Element) e)));
        return renders;
    }

    public String renderElement(Element element) {
        return getElementRender(element).render(element);
    }

    public TextRender getTextRender(Text text) {
        Parameter parameter = text.getParameter(RenderKeys.TEXT_TYPE);
        if (parameter == null) {
            return primaryTextRender;
        }
        String type = parameter.getValue();
        for (TextRender render : textRenders) {
            if (render.support(type)) {
                return render;
            }
        }
        throw JSlideException.builder().message("Can't render text type: " + type).build();
    }

    public Collection<TextRender> getTextRenders(Element element) {
        Set<TextRender> renders = new HashSet<>();
        element.getChildren().forEach(n -> {
            if (n instanceof Text) {
                renders.add(getTextRender((Text) n));
            } else if (n instanceof Element) {
                renders.addAll(getTextRenders((Element) n));
            }
        });
        return renders;
    }

    public String renderText(Text text) {
        return getTextRender(text).render(text);
    }

    public String renderView(String template, Map<String, Object> model) {
        try {
            Template t = freemarkerConfiguration.getTemplate(template);
            StringWriter out = new StringWriter();
            t.process(model, out);
            return out.toString();
        } catch (TemplateException | IOException e) {
            throw JSlideException.builder()
                    .message("Fail to render view: " + template)
                    .cause(e)
                    .build();
        }
    }
}
