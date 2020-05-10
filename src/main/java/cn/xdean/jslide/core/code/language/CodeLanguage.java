package cn.xdean.jslide.core.code.language;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Value
@Builder
public class CodeLanguage {
    String name;

    @Singular
    List<String> extensions;

    String mime;

    String codeMirrorJs;

    @Builder.Default
    Predicate<String> contentMatch = s -> false;

}
