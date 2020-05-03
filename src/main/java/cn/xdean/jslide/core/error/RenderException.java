package cn.xdean.jslide.core.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RenderException extends RuntimeException {
    private final int index;

    @Builder
    public RenderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int index) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.index = index;
    }
}
