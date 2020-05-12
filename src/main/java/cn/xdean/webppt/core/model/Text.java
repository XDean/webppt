package cn.xdean.webppt.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = "parent")
@EqualsAndHashCode(exclude = "parent")
public class Text implements Node {
    Element parent;

    List<String> lines = new ArrayList<>();

    final RawInfo rawInfo = new RawInfo();

    @Override
    public String getName() {
        return "text";
    }

    public Parameter getParameter(String key) {
        return parent.getParameter(key, this);
    }
}
