package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.render.*;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import xdean.jex.extra.collection.Either;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractElementRender implements ElementRender {

    protected final List<String> names;
    protected @Autowired RenderService renderService;
    protected @Setter boolean renderChildElement = true;
    protected @Setter boolean renderChildText = true;

    public AbstractElementRender(String... names) {
        this.names = Arrays.asList(names);
    }

    @Override
    public boolean support(String name) {
        return names.contains(name);
    }

    protected ModelMap getDefaultModelMap(Element element) {
        return new ModelMap()
                .addAttribute(RenderKeys.ELEMENT, element).addAttribute(RenderKeys.CHILDREN, processChildren(element));
    }

    protected List<String> processChildren(Element element) {
        List<String> res = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        for (Either<Element, String> child : element.getChildren()) {
            if (child.isLeft()) {
                if (renderChildText && !lines.isEmpty()) {
                    if (lines.stream().anyMatch(s -> !s.isEmpty())) {
                        res.add(renderService.renderText(element, lines));
                    }
                    lines = new ArrayList<>();
                }
                if (renderChildElement) {
                    res.add(renderService.renderElement(child.getLeft()));
                }
            } else {
                lines.add(child.getRight());
            }
        }
        if (renderChildText && !lines.isEmpty()) {
            res.add(renderService.renderText(element, lines));
        }
        return res;
    }
}
