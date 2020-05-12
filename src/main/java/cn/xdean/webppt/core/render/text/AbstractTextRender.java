package cn.xdean.webppt.core.render.text;

import cn.xdean.webppt.core.render.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

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
