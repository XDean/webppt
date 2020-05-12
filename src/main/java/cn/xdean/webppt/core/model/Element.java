package cn.xdean.webppt.core.model;

import io.reactivex.Observable;
import io.reactivex.functions.Predicate;
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
        return getParameter(key, this);
    }

    public String getParameter(String key, String defaultValue) {
        return getParameter(key, this, defaultValue);
    }

    @Nullable
    public Parameter getParameter(String key, Node node) {
        Parameter parameter = Observable.fromIterable(children)
                .takeUntil((Predicate<Node>) e -> e.contains(node))
                .filter(n -> n instanceof Parameter)
                .cast(Parameter.class)
                .filter(e -> e.support(node) && e.getKey().equals(key))
                .lastElement()
                .blockingGet();
        if (parameter == null) {
            if (parent == this) {
                return null;
            } else {
                return parent.getParameter(key, node);
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

    @Override
    public boolean contains(Node node) {
        return node == this || this.getChildren().stream().anyMatch(e -> e.contains(node));
    }
}
