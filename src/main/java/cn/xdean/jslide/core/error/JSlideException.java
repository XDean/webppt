package cn.xdean.jslide.core.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JSlideException extends RuntimeException {
    private final int line;

    @Builder
    public JSlideException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int line) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.line = line;
    }
}
