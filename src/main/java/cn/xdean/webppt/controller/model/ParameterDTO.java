package cn.xdean.webppt.controller.model;

import cn.xdean.webppt.core.model.Parameter;
import cn.xdean.webppt.core.model.RawInfo;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParameterDTO {
    String element;

    String key;

    String value;

    RawInfo rawInfo;

    public static ParameterDTO from(Parameter p) {
        return ParameterDTO.builder()
                .element(p.getElement())
                .key(p.getKey())
                .value(p.getValue())
                .rawInfo(p.getRawInfo())
                .build();
    }
}
