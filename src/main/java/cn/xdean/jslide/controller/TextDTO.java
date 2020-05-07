package cn.xdean.jslide.controller;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.model.Node;
import cn.xdean.jslide.core.model.RawInfo;
import cn.xdean.jslide.core.model.Text;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TextDTO {

    @Singular
    List<String> lines;

    RawInfo rawInfo;

    public static TextDTO from(Text e) {
        return TextDTO.builder()
                .lines(e.getLines())
                .rawInfo(e.getRawInfo())
                .build();
    }
}
