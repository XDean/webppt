package cn.xdean.webppt.core.code.play;

import io.reactivex.Observable;
import lombok.Builder;
import lombok.Value;

import java.util.List;

public interface CodePlayer {

    String getName();

    List<String> getExtensions();

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
