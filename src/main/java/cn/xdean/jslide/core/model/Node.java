package cn.xdean.jslide.core.model;

import javax.annotation.Nullable;

public interface Node {

    String getName();

    @Nullable
    Element getParent();

    void setParent(Element element);

    RawInfo getRawInfo();

    @Nullable
    Parameter getParameter(String key);
}
