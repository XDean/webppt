package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.service.render.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ui.ModelMap;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractRender implements Render, BeanFactoryAware, InitializingBean {

    protected final List<String> names;
    protected BeanFactory beanFactory;
    protected RenderService renderService;

    public AbstractRender(String... names) {
        this.names = Arrays.asList(names);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.renderService = beanFactory.getBean(RenderService.class);
    }

    @Override
    public boolean support(String name) {
        return names.contains(name);
    }

    protected ModelMap getDefaultModelMap(Element element) {
        return new ModelMap()
                .addAttribute(RenderKeys.ELEMENT, element)
                .addAttribute(RenderKeys.CHILDREN, processChildren(element));
    }

    protected List<RenderLine> processChildren(Element element) {
        return element.getChildren()
                .stream()
                .map(c -> c.unify(
                        e -> RenderLine.element(renderService.renderElement(e)),
                        e -> RenderLine.line(e)))
                .collect(Collectors.toList());
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
