package cn.xdean.jslide.core.code.play;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCodePlayer implements CodePlayer {

    @Getter
    private final String name;

    @Getter
    private final List<String> extensions;

    public AbstractCodePlayer(String name, String... extensions) {
        this.name = name;
        this.extensions = Arrays.asList(extensions);
    }
}
