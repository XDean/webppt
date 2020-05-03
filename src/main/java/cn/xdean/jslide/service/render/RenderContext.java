package cn.xdean.jslide.service.render;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
public class RenderContext {
    public final Set<String> scripts = new LinkedHashSet<>();

    public final Set<String> styles = new LinkedHashSet<>();
}
