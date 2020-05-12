package cn.xdean.webppt.core.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final int line;

    @Builder
    public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int line) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.line = line;
    }
}
