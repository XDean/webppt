package cn.xdean.jslide.service.render.provider;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.error.RenderException;
import cn.xdean.jslide.service.render.RenderKeys;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.List;

@Component
public class ImageRender extends AbstractRender {
    public ImageRender() {
        super("img", "image");
    }

    @Override
    public String render(Element element) {
        List<Element> elements = element.getElements();
        if (!elements.isEmpty()) {
            throw RenderException.builder()
                    .index(elements.get(0).getLineIndex())
                    .message("image can't have child element")
                    .build();
        }
        String typeName = element.resolveParameter("type", ImageType.URL.toString());
        ImageType type = EnumUtils.getEnum(ImageType.class, typeName);
        if (type == null) {
            throw RenderException.builder()
                    .index(element.getLineIndex())
                    .message("unknown image type: " + typeName)
                    .build();
        }
        String content = type.resolve(element);
        return renderService.renderView("image.ftlh", getDefaultModelMap(element)
                .addAttribute(RenderKeys.MODEL, ImageModel.builder()
                        .type(type)
                        .content(content)
                        .width(element.resolveParameter("width"))
                        .height(element.resolveParameter("height"))
                        .style(element.resolveParameter("style"))
                        .alt(element.resolveParameter("alt"))
                        .attributes(element.resolveParameter("attributes"))
                        .build()));
    }

    public enum ImageType {
        URL {
            @Override
            public String resolve(Element element) {
                List<String> lines = element.getLines();
                if (lines.size() != 1) {
                    throw RenderException.builder()
                            .index(element.getLineIndex())
                            .message("image(type=url) must provide exact 1 url")
                            .build();
                }
                return lines.get(0);
            }
        },
        BASE64 {
            @Override
            public String resolve(Element element) {
                return String.join("", element.getLines());
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
