package cn.xdean.jslide.core.render.element;

import cn.xdean.jslide.core.model.Element;
import lombok.Builder;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value
@Builder
public class CommonElementModel {
    @Nullable String width;
    @Nullable String height;
    @Nullable String style;
    @Nullable String attributes;
    @Nullable String classes;

    public static CommonElementModel from(Element element) {
        return CommonElementModel.builder()
                .width(element.getParameter("width", (String) null))
                .height(element.getParameter("height", (String) null))
                .style(element.getParameter("style", (String) null))
                .classes(element.getParameter("class", (String) null))
                .attributes(element.getParameter("attributes", (String) null))
                .build();
    }
}
