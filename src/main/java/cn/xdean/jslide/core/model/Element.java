package cn.xdean.jslide.core.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.reactivex.Observable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString(exclude = "parent")
@EqualsAndHashCode(exclude = "parent")
public class Element implements Node {

    String name;

    Element parent = this;

    final Deque<Node> children = new ArrayDeque<>();

    final RawInfo rawInfo = new RawInfo();

    @Override
    public Parameter getParameter(String key) {
        return getParameter(key, (Node) null);
    }

    public String getParameter(String key, String defaultValue) {
        return getParameter(key, null, defaultValue);
    }

    @Nullable
    public Parameter getParameter(String key, Node pos) {
        Parameter parameter = Observable.fromIterable(children)
                .takeUntil(e -> e == pos)
                .filter(n -> n instanceof Parameter)
                .cast(Parameter.class)
                .filter(e -> e.getKey().equals(key))
                .lastElement()
                .blockingGet();
        if (parameter == null) {
            if (parent == this) {
                return null;
            } else {
                return parent.getParameter(key, this);
            }
        } else {
            return parameter;
        }
    }

    public String getParameter(String key, Node pos, String defaultValue) {
        Parameter parameter = getParameter(key, pos);
        if (parameter == null) {
            return defaultValue;
        } else {
            return parameter.getValue();
        }
    }

    public List<Text> getTexts() {
        return children.stream()
                .filter(e -> e instanceof Text)
                .map(e -> (Text) e)
                .collect(Collectors.toList());
    }

    public List<Element> getElements() {
        return children.stream()
                .filter(e -> e instanceof Element)
                .map(e -> (Element) e)
                .collect(Collectors.toList());
    }

    public List<Parameter> getParameters() {
        return children.stream()
                .filter(e -> e instanceof Parameter)
                .map(e -> (Parameter) e)
                .collect(Collectors.toList());
    }

    public boolean isRoot() {
        return name.equals("root") && parent == this;
    }

    public boolean isDeep(int i) {
        Assert.isTrue(i >= 0, "deep must non-negative");
        if (i == 0) {
            return isRoot();
        }
        return parent != this && parent.isDeep(i - 1);
    }
}
