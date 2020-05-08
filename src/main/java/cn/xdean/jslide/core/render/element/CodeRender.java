package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.render.RenderContext;
import cn.xdean.jslide.core.render.RenderKeys;
import lombok.Builder;
import lombok.Value;
import org.springframework.stereotype.Component;

@Component
public class CodeRender extends TemplateElementRender {
    public CodeRender() {
        super("code");
    }

    @Override
    protected void preRenderCheck(Element element) {
        assertNoChildElement(element);
        assertSingleText(element);
        assertParameterFirst(element);
    }

    @Override
    protected Object generateModel(RenderContext ctx, Element element) {
        String code = String.join("\n", element.getTexts().get(0).getLines());
        return CodeModel.builder()
                .id(element.getRawInfo().getStartLineIndex())
                .content(code)
                .build();
    }

    @Override
    public void initContext(RenderContext context) {
        context.scripts.add("/static/webjars/codemirror/5.53.2/lib/codemirror.js");
        context.scripts.add("/static/js/code.js");
        context.styles.add("/static/webjars/codemirror/5.53.2/lib/codemirror.css");
        context.styles.add("/static/webjars/codemirror/5.53.2/theme/idea.css");
    }

    @Value
    @Builder
    public static class CodeModel {
        int id;
        String content;
    }
}
