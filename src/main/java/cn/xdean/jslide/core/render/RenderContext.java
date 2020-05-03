package cn.xdean.jslide.core.render;

import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class RenderContext {
    public final Set<String> scripts = new LinkedHashSet<>();

    public final Set<String> styles = new LinkedHashSet<>();

    public final Set<String> globalElements = new LinkedHashSet<>();
}
