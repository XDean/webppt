package cn.xdean.jslide.core.render.text;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.render.*;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTextRender implements TextRender {

    protected final List<String> names;
    protected @Autowired RenderService renderService;

    public AbstractTextRender(String... names) {
        this.names = Arrays.asList(names);
    }

    @Override
    public boolean support(String name) {
        return names.contains(name);
    }
}
