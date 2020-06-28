package cn.xdean.webppt.core.code.run;

import io.reactivex.Observable;
import lombok.Builder;
import lombok.Value;

public interface CodeRunner {

    String name();

    boolean isSupport();

    Observable<Line> run(String code);

    @Value
    @Builder
    class Line {

        public enum Type {
            STDOUT,
            STDERR,

            DONE,
            ERROR,
            START,
            STOP;

            public Line of(String message) {
                return Line.builder().type(this).message(message).build();
            }

            public Line of(int exitCode) {
                return Line.builder().type(this).message(Integer.toString(exitCode)).build();
            }
        }

        Type type;
        String message;

    }
}
