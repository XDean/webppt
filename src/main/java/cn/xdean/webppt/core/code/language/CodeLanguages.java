package cn.xdean.webppt.core.code.language;

import cn.xdean.webppt.core.code.run.impl.GoCodeRunner;
import cn.xdean.webppt.core.code.run.impl.JavaCodeRunner;
import cn.xdean.webppt.core.code.run.impl.PythonCodeRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CodeLanguages {
    @Bean
    public CodeLanguage java(JavaCodeRunner runner) {
        return CodeLanguage.builder()
                .name("java")
                .extension("java")
                .mime("text/x-java")
                .codeMirrorJs("clike/clike.js")
                .runner(runner)
                .build();
    }

    @Bean
    public CodeLanguage golang(GoCodeRunner runner) {
        return CodeLanguage.builder()
                .name("go")
                .extension("go")
                .mime("text/x-go")
                .codeMirrorJs("go/go.js")
                .runner(runner)
                .build();
    }

    @Bean
    public CodeLanguage python(PythonCodeRunner runner) {
        return CodeLanguage.builder()
                .name("python")
                .extension("py")
                .mime("text/x-python")
                .codeMirrorJs("python/python.js")
                .runner(runner)
                .build();
    }
}
