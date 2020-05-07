package cn.xdean.jslide.controller;

import cn.xdean.jslide.core.model.Parameter;
import cn.xdean.jslide.core.model.RawInfo;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParameterDTO {
    String key;

    String value;

    RawInfo rawInfo;

    public static ParameterDTO from(Parameter p) {
        return ParameterDTO.builder()
                .key(p.getKey())
                .value(p.getValue())
                .rawInfo(p.getRawInfo())
                .build();
    }
}
