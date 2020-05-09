package cn.xdean.jslide.core.model;

import lombok.*;

import javax.annotation.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "parent")
@EqualsAndHashCode(exclude = "parent")
public class Parameter implements Node {

    Element parent;

    @Nullable
    String element;

    String key;

    @Nullable
    String value;

    final RawInfo rawInfo = new RawInfo();

    @Override
    public String getName() {
        return "parameter";
    }

    @Override
    public Parameter getParameter(String key) {
        return parent.getParameter(key, this);
    }

    public boolean support(Node node) {
        return this.element == null || this.element.equals(node.getName());
    }
}
