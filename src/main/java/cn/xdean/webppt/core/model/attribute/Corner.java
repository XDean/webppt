package cn.xdean.webppt.core.model.attribute;

import lombok.Getter;

@Getter
public enum Corner {
    LEFT_TOP(Horizontal.LEFT, Vertical.TOP),
    LEFT_CENTER(Horizontal.LEFT, Vertical.CENTER),
    LEFT_BOTTOM(Horizontal.LEFT, Vertical.BOTTOM),
    CENTER_TOP(Horizontal.CENTER, Vertical.TOP),
    CENTER_CENTER(Horizontal.CENTER, Vertical.CENTER),
    CENTER_BOTTOM(Horizontal.CENTER, Vertical.BOTTOM),
    RIGHT_TOP(Horizontal.RIGHT, Vertical.TOP),
    RIGHT_CENTER(Horizontal.RIGHT, Vertical.CENTER),
    RIGHT_BOTTOM(Horizontal.RIGHT, Vertical.BOTTOM);

    public final Horizontal horizontal;
    public final Vertical vertical;

    Corner(Horizontal horizontal, Vertical vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}