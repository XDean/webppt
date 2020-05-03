package cn.xdean.jslide.service.render;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.error.ParseException;
import cn.xdean.jslide.model.error.RenderException;
import cn.xdean.jslide.service.render.provider.RootRenderProvider;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

@Service
public class RenderService {
    @Autowired List<RenderProvider> providers;
    @Autowired Configuration freemarkerConfiguration;
    @Autowired RootRenderProvider rootRenderProvider;

    public RenderProvider getProvider(String name) {
        for (RenderProvider provider : providers) {
            if (provider.support(name)) {
                return provider;
            }
        }
        throw ParseException.builder().message("Can't render element: " + name).build();
    }

    public String renderElement(Element element) {
        return getProvider(element.getName()).render(element);
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
