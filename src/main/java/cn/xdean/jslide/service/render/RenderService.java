package cn.xdean.jslide.service.render;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.error.ParseException;
import cn.xdean.jslide.service.render.RenderProvider;
import com.google.common.collect.Maps;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RenderService {
    @Autowired List<RenderProvider> providers;


    public String render(Element element) {
        for (RenderProvider provider : providers) {
            if (provider.support(element.getName())) {
                String render = provider.render(element);
                return StringSubstitutor.replace(render, Collections.singletonMap("children", element.getChildren()
                        .stream()
                        .map(this::render)
                        .collect(Collectors.joining("\n"))));
            }
        }
        throw ParseException.builder().message("Can't render element: " + element.getName()).build();
    }
}
