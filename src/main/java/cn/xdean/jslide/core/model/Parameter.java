package cn.xdean.jslide.core.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "parent")
@EqualsAndHashCode(exclude = "parent")
public class Parameter implements Node {

    Element parent;

    String key;

    String value;

    final RawInfo rawInfo = new RawInfo();

    @Override
    public Parameter getParameter(String key) {
        return parent.getParameter(key, this);
    }
}
