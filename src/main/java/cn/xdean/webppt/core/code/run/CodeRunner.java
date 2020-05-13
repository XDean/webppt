package cn.xdean.webppt.core.code.run;

import io.reactivex.Observable;
import lombok.Builder;
import lombok.Value;

public interface CodeRunner {

    Observable<Line> run(String code);

    @Value
    @Builder
    class Line {

        enum Type {
            STDOUT,
            STDERR,
            SYSTEM;
        }

        Type type;
        String message;

    }
}
