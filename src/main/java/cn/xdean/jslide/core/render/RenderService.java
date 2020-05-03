package cn.xdean.jslide.core.render;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.error.ParseException;
import cn.xdean.jslide.core.error.RenderException;
import cn.xdean.jslide.core.render.provider.RootRender;
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
    @Autowired List<Render> providers;
    @Autowired Configuration freemarkerConfiguration;
    @Autowired RootRender rootRenderProvider;

    public Render getProvider(Element element) {
        for (Render provider : providers) {
            if (provider.support(element.getName())) {
                return provider;
            }
        }
        throw ParseException.builder().message("Can't render element: " + element.getName()).build();
    }

    public Collection<Render> getAllProviders(Element element) {
        Set<Render> renders = new HashSet<>();
        renders.add(getProvider(element));
        element.getChildren().forEach(c -> c.ifLeft(e -> renders.addAll(getAllProviders(e))));
        return renders;
    }

    public String renderElement(Element element) {
        return getProvider(element).render(element);
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
