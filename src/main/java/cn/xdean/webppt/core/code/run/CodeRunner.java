package cn.xdean.webppt.core.code.run;

import io.reactivex.Observable;
import lombok.Builder;
import lombok.Value;

public interface CodeRunner {

    default boolean available() {
        return true;
    }

    Observable<Line> run(String code);

    @Value
    @Builder
    class Line {

        public enum Type {
            STDOUT,
            STDERR,

            SYSTEM,
            STATUS;

            public Line of(String message) {
                return Line.builder().type(this).message(message).build();
            }
        }

        Type type;
        String message;

    }
}
