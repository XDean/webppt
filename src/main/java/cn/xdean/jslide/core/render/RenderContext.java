package cn.xdean.jslide.core.render;

import cn.xdean.jslide.core.error.JSlideException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Value
@Builder
public class RenderContext {

    public Resource resource;

    public List<Exception> warnings = new ArrayList<>();

    public Set<String> scripts = new LinkedHashSet<>();

    public Set<String> styles = new LinkedHashSet<>();

    public Set<String> globalElements = new LinkedHashSet<>();
}
