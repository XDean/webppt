package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.error.JSlideException;
import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.render.RenderContext;
import cn.xdean.jslide.core.render.RenderKeys;
import lombok.Builder;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;

@Component
public class ImageRender extends TemplateElementRender {
    public ImageRender() {
        super("image", "img");
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
                .width(resolveParameter(element, "width", null))
                .height(resolveParameter(element, "height", null))
                .style(resolveParameter(element, "style", null))
                .alt(resolveParameter(element, "alt", null))
                .attributes(resolveParameter(element, "attributes", null))
                .build();
    }

    public enum ImageType {
        URL {
            @Override
            public String resolve(Element element) {
                List<String> lines = element.getTexts().get(0).getLines();
                if (lines.size() != 1) {
                    throw JSlideException.builder()
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
        @Nullable String width;
        @Nullable String height;
        @Nullable String style;
        @Nullable String alt;
        @Nullable String attributes;
    }
}
