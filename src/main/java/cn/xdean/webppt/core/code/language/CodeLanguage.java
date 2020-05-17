package cn.xdean.webppt.core.code.language;

import cn.xdean.webppt.core.code.run.CodeRunner;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.List;

@Value
@Builder
public class CodeLanguage {
    String name;

    @Singular
    List<String> extensions;

    String mime;

    String codeMirrorJs;

    @Nullable
    CodeRunner runner;
}
