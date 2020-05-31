package cn.xdean.webppt.core.model.attribute;

public enum Horizontal {
    LEFT(-1),
    CENTER(0),
    RIGHT(1);

    public final int pos;

    Horizontal(int i) {
        this.pos = i;
    }
}
