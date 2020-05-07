package cn.xdean.jslide.core.model;

import javax.annotation.Nullable;

public interface Node {
    @Nullable
    Element getParent();

    void setParent(Element element);

    RawInfo getRawInfo();

    @Nullable
    Parameter getParameter(String key);
}
