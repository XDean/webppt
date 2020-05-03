package cn.xdean.jslide.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.NonFinal;
import org.springframework.util.Assert;
import xdean.jex.extra.collection.Either;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
@ToString(exclude = "parent")
@EqualsAndHashCode(exclude = "parent")
public class Element {
    int lineIndex;

    String name;

    @JsonIgnore
    @Nullable
    @NonFinal
    @Setter
    Element parent;

    Map<String, String> parameters;

    List<Either<Element, String>> children;

    @Builder
    public Element(int lineIndex, String name, @Singular Map<String, String> parameters,
                   @Singular List<Either<Element, String>> children) {
        this.lineIndex = lineIndex;
        this.name = name;
        this.parameters = parameters;
        this.children = children;
        children.forEach(c -> c.ifLeft(e -> e.setParent(this)));
    }

    public Map<String, String> resolveParameters() {
        Map<String, String> res = new HashMap<>();
        if (parent != null) {
            res.putAll(parent.resolveParameters());
        }
        res.putAll(this.parameters);
        return res;
    }

    @Nullable
    public String resolveParameter(String key) {
        String value = parameters.get(key);
        if (value == null) {
            if (parent == null) {
                return null;
            } else {
                return parent.resolveParameter(key);
            }
        } else {
            return value;
        }
    }

    public String resolveParameter(String key, String defaultValue) {
        String value = parameters.get(key);
        if (value == null) {
            if (parent == null) {
                return defaultValue;
            } else {
                return parent.resolveParameter(key, defaultValue);
            }
        } else {
            return value;
        }
    }

    public List<String> getLines() {
        return children.stream()
                .filter(e -> e.isRight())
                .map(e -> e.getRight())
                .collect(Collectors.toList());
    }

    public List<Element> getElements() {
        return children.stream()
                .filter(e -> e.isLeft())
                .map(e -> e.getLeft())
                .collect(Collectors.toList());
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

    public static class ElementBuilder {
        public ElementBuilder line(String line) {
            return child(Either.right(line));
        }

        public ElementBuilder element(Element element) {
            return child(Either.left(element));
        }
    }
}
