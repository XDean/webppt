package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.model.Node;
import cn.xdean.jslide.core.model.Text;
import cn.xdean.jslide.core.render.RenderContext;
import cn.xdean.jslide.core.render.RenderKeys;
import lombok.Setter;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

public abstract class TemplateElementRender extends AbstractElementRender {

    protected @Setter String template;
    protected @Setter boolean renderChildElement = true;
    protected @Setter boolean renderChildText = true;

    public TemplateElementRender(String... names) {
        super(names);
        setTemplate("element/" + names[0] + ".ftlh");
    }

    @Override
    public String render(RenderContext ctx, Element element) {
        preRenderCheck(element);
        return renderService.renderView(template, getTemplateModel(ctx, element));
    }

    protected void preRenderCheck(Element element) {

    }

    protected abstract Object generateModel(RenderContext ctx, Element element);

    protected ModelMap getTemplateModel(RenderContext ctx, Element element) {
        return new ModelMap()
                .addAttribute(RenderKeys.MODEL, generateModel(ctx, element))
                .addAttribute(RenderKeys.ELEMENT, element)
                .addAttribute(RenderKeys.CHILDREN, processChildren(ctx, element));
    }


    protected List<String> processChildren(RenderContext ctx, Element element) {
        List<String> res = new ArrayList<>();
        for (Node node : element.getChildren()) {
            if (node instanceof Element) {
                if (renderChildElement) {
                    res.add(renderService.renderElement((Element) node));
                }
            } else if (node instanceof Text) {
                if (renderChildText) {
                    res.add(renderService.renderText((Text) node));
                }
            }
        }
        return res;
    }
}
