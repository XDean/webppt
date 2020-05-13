package cn.xdean.webppt.core.code.language;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CodeLanguage {
    String name;

    @Singular
    List<String> extensions;

    String mime;

    String codeMirrorJs;

}
