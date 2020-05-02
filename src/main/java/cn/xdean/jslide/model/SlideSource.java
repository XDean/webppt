package cn.xdean.jslide.model;

import cn.xdean.jslide.service.resource.Resource;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SlideSource {
    String content;
    Resource resource;
}
