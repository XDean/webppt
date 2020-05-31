package cn.xdean.webppt.core.render.element.layout;

import cn.xdean.webppt.core.model.Element;
import cn.xdean.webppt.core.model.attribute.Corner;
import cn.xdean.webppt.core.render.RenderContext;
import cn.xdean.webppt.core.render.element.CommonElementModel;
import cn.xdean.webppt.core.render.element.TemplateElementRender;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class BoxRender extends TemplateElementRender {
    public BoxRender() {
        super("box");
    }

    @Override
    public boolean support(String name) {
        return "hbox".equals(name) || "vbox".equals(name);
    }

    @Override
    protected BoxModel generateModel(RenderContext ctx, Element element) {
        return BoxModel.builder()
                .common(CommonElementModel.from(element))
                .horizontal(element.getName().equals("hbox"))
                .align(resolveParameter(element, Corner.class, "align", Corner.CENTER_CENTER))
                .build();
    }

    @Override
    public void initContext(RenderContext context) {
        context.styles.add("/static/css/box.css");
    }

    @Data
    @Builder
    public static class BoxModel {
        boolean horizontal;
        Corner align;
        CommonElementModel common;
    }
}
