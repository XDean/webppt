package cn.xdean.jslide.model;

import lombok.Builder;
import lombok.Setter;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Value
public class Element {
    int lineIndex;

    String name;

    @Nullable
    @NonFinal
    @Setter
    Element parent;

    Map<String, String> parameters;

    List<Element> children;

    List<String> lines;

    @Builder
    public Element(int lineIndex, String name, @Singular Map<String, String> parameters,
                   @Singular List<Element> children, @Singular List<String> lines) {
        this.lineIndex = lineIndex;
        this.name = name;
        this.parameters = parameters;
        this.children = children;
        this.lines = lines;
        children.forEach(e -> e.setParent(this));
    }

    public String getValue(String key) {
        String value = parameters.get(key);
        if (value == null) {
            if (parent == null) {
                return "";
            } else {
                return parent.getValue(key);
            }
        } else {
            return value;
        }
    }

    public boolean isRoot() {
        return name.equals("root");
    }

    public boolean isDeep(int i) {
        Assert.isTrue(i >= 0, "deep must non-negative");
        if (i == 0) {
            return isRoot();
        }
        return parent != null && parent.isDeep(i - 1);
    }
}
