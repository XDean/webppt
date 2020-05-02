package cn.xdean.jslide.model.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ParseException extends RuntimeException {
    private final int index;
    private final String line;

    @Builder
    public ParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int index, String line) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.index = index;
        this.line = line;
    }
}
