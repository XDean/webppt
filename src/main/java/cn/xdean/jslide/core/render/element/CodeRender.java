package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.error.JSlideException;
import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.render.RenderContext;
import lombok.Builder;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.List;

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
    protected CodeModel generateModel(RenderContext ctx, Element element) {
        CodeType type = resolveParameter(element, CodeType.class, "type", CodeType.URL);
        String code = type.resolve(element);
        String theme = resolveParameter(element, "theme", "idea");
        ctx.styles.add(String.format("/static/webjars/codemirror/5.53.2/theme/%s.css", theme));
        return CodeModel.builder()
                .id(element.getRawInfo().getStartLineIndex())
                .type(type)
                .content(code)
                .theme(theme)
                .common(CommonElementModel.from(element))
                .build();
    }

    @Override
    public void initContext(RenderContext context) {
        context.scripts.add("/static/webjars/codemirror/5.53.2/lib/codemirror.js");
        context.styles.add("/static/webjars/codemirror/5.53.2/lib/codemirror.css");

        context.scripts.add("/static/webjars/codemirror/5.53.2/addon/scroll/simplescrollbars.js");
        context.styles.add("/static/webjars/codemirror/5.53.2/addon/scroll/simplescrollbars.css");

        context.scripts.add("/static/js/code.js");
        context.styles.add("/static/css/code.css");
    }

    public enum CodeType {
        URL {
            @Override
            public String resolve(Element element) {
                List<String> lines = element.getTexts().get(0).getLines();
                if (lines.size() != 1) {
                    throw JSlideException.builder()
                            .line(element.getRawInfo().getStartLineIndex())
                            .message("(type=url) must has exact 1 line url")
                            .build();
                }
                return lines.get(0);
            }
        },
        TEXT {
            @Override
            public String resolve(Element element) {
                return String.join("\n", element.getTexts().get(0).getLines());
            }
        };

        public abstract String resolve(Element element);
    }

    @Value
    @Builder
    public static class CodeModel {
        int id;
        CodeType type;
        String content;
        String theme;
        CommonElementModel common;
    }
}
