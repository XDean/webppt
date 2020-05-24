package cn.xdean.webppt.core.render.element;

import cn.xdean.webppt.core.code.language.CodeLanguage;
import cn.xdean.webppt.core.code.language.CodeLanguageService;
import cn.xdean.webppt.core.error.AppException;
import cn.xdean.webppt.core.model.Element;
import cn.xdean.webppt.core.render.RenderContext;
import lombok.Builder;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CodeRender extends TemplateElementRender {

    @Autowired ResourceLoader resourceLoader;
    @Autowired CodeLanguageService codeLanguageService;

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
        String content = type.getContent(element);

        String theme = resolveParameter(element, "theme", "idea");
        ctx.styles.add(String.format("/static/webjars/codemirror/5.53.2/theme/%s.css", theme));

        String lang = resolveParameter(element, "lang", null);
        CodeLanguage language = null;
        if (lang == null) {
            switch (type) {
                case URL:
                    try {
                        language = codeLanguageService.getLanguageByResource(ctx.resource.createRelative(content)).orElse(null);
                    } catch (IOException e) {
                        ctx.warnings.add(e);
                    }
                    break;
                case TEXT:
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        } else {
            language = codeLanguageService.getLanguageByName(lang).orElse(null);
        }
        if (language == null) {
            ctx.warnings.add(AppException.builder()
                    .message("Can't resolve code language")
                    .line(element.getRawInfo().getStartLineIndex())
                    .build());
        } else {
            ctx.scripts.add("/static/webjars/codemirror/5.53.2/mode/" + language.getCodeMirrorJs());
        }

        return CodeModel.builder()
                .id(element.getRawInfo().getStartLineIndex())
                .type(type)
                .language(language)
                .content(content)
                .theme(theme)
                .common(CommonElementModel.from(element))
                .readonly(resolveParameter(element, boolean.class, "readonly", false))
                .play(resolveParameter(element, boolean.class, "play", false))
                .resize(resolveParameter(element, boolean.class, "resize", false))
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
            public String getContent(Element element) {
                List<String> lines = element.getTexts().get(0).getLines();
                if (lines.size() != 1) {
                    throw AppException.builder()
                            .line(element.getRawInfo().getStartLineIndex())
                            .message("(type=url) must has exact 1 line url")
                            .build();
                }
                return lines.get(0);
            }
        },
        TEXT {
            @Override
            public String getContent(Element element) {
                return String.join("\n", element.getTexts().get(0).getLines());
            }
        };

        public abstract String getContent(Element element);

    }

    @Value
    @Builder
    public static class CodeModel {
        int id;
        boolean readonly;
        boolean resize;
        boolean play;
        CodeType type;
        CodeLanguage language;
        String content;
        String theme;
        CommonElementModel common;
    }
}
