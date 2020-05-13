package cn.xdean.webppt.core.code.run;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCodeRunner implements CodeRunner {

    @Getter
    private final String name;

    @Getter
    private final List<String> extensions;

    public AbstractCodeRunner(String name, String... extensions) {
        this.name = name;
        this.extensions = Arrays.asList(extensions);
    }
}
