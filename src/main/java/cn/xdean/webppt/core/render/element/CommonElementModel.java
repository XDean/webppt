package cn.xdean.webppt.core.render.element;

import cn.xdean.webppt.core.model.Element;
import lombok.Builder;
import lombok.Value;

import javax.annotation.Nullable;

@Value
@Builder
public class CommonElementModel {
    @Nullable String width;
    @Nullable String height;
    @Nullable String style;
    @Nullable String classes;
    @Nullable String attributes;

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
