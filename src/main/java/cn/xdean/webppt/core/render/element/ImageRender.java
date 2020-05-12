package cn.xdean.webppt.core.render.element;

import cn.xdean.webppt.core.error.AppException;
import cn.xdean.webppt.core.model.Element;
import cn.xdean.webppt.core.render.RenderContext;
import lombok.Builder;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImageRender extends TemplateElementRender {
    public ImageRender() {
        super("image");
    }

    @Override
    protected void preRenderCheck(Element element) {
        assertNoChildElement(element);
        assertParameterFirst(element);
        assertSingleText(element);
    }

    @Override
    protected ImageModel generateModel(RenderContext ctx, Element element) {
        ImageType type = resolveParameter(element, ImageType.class, "type", ImageType.URL);
        String content = type.resolve(element);
        return ImageModel.builder()
                .type(type)
                .content(content)
                .common(CommonElementModel.from(element))
                .build();
    }

    public enum ImageType {
        URL {
            @Override
            public String resolve(Element element) {
                List<String> lines = element.getTexts().get(0).getLines();
                if (lines.size() != 1) {
                    throw AppException.builder()
                            .line(element.getRawInfo().getStartLineIndex())
                            .message("image(type=url) must has exact 1 line url")
                            .build();
                }
                return lines.get(0);
            }
        },
        BASE64 {
            @Override
            public String resolve(Element element) {
                return String.join("", element.getTexts().get(0).getLines());
            }
        };

        public abstract String resolve(Element element);
    }

    @Value
    @Builder
    public static class ImageModel {
        ImageType type;
        String content;
        CommonElementModel common;
    }
}
