package cn.xdean.webppt.core.model.attribute;

public enum Vertical {
    TOP(-1),
    CENTER(0),
    BOTTOM(1);

    public final int pos;

    Vertical(int i) {
        this.pos = i;
    }
}
