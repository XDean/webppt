package cn.xdean.jslide.service;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.Slide;
import cn.xdean.jslide.model.SlideSource;
import cn.xdean.jslide.model.error.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.StringTokenizer;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;

@Service
public class ParseService {
    public Slide parse(SlideSource source) {
        return new Parser(source).parse();
    }

    private class Parser {
        final SlideSource source;
        final Slide.SlideBuilder slideBuilder = Slide.builder();
        final String[] lines;

        int index;
        String line;
        boolean consumed;
        Deque<Element.ElementBuilder> elemStack = new ArrayDeque<>();

        Parser(SlideSource source) {
            this.source = source;
            this.lines = source.getContent().split("\\R");
        }

        Slide parse() {
            while (nextLine()) {
                if (line.isEmpty()) {
                    continue;
                }
                switch (line.charAt(0)) {
                    case '.':
                        parseComponent();
                        break;
                    case '@':
                        parseParameter();
                        break;
                    case '}':
                        parseEndTag();
                        break;
                    case '#': // comment
                        consumed = true;
                        break;
                }
                if (!consumed) {
                    if (elemStack.isEmpty()) {
                        throw ParseException.builder()
                                .index(index)
                                .line(line)
                                .message("plain text content must be in component")
                                .build();
                    } else {
                        elemStack.getLast().line(line);
                    }
                }
            }
            return slideBuilder.build();
        }

        boolean nextLine() {
            if (index == lines.length - 1) {
                return false;
            }
            index++;
            line = lines[index].trim();
            consumed = false;
            return true;
        }


        void parseParameter() {
            Pair<String, String> parameter = ParseService.parseParameter(line.substring(1));
            if (elemStack.isEmpty()) {
                slideBuilder.parameter(parameter.getKey(), parameter.getValue());
            } else {
                elemStack.getLast().parameter(parameter.getKey(), parameter.getValue());
            }
            consumed = true;
        }

        void parseComponent() {
            int index = StringUtils.indexOfAny(line, "{ ");
            Element.ElementBuilder elem = Element.builder();
            if (index == -1) {
                elem.name(line.substring(1));
            } else {
                elem.name(line.substring(1, index));
            }
            if (index != -1) {
                if (line.charAt(index) == ' ') {
                    StringTokenizer t = new StringTokenizer(line.substring(index + 1), ' ', '"');
                    while (t.hasNext()) {
                        String next = t.next();
                        if (next.startsWith("@")) {
                            Pair<String, String> parameter = ParseService.parseParameter(next.substring(1));
                            elem.parameter(parameter.getKey(), parameter.getValue());
                        } else {
                            elem.line(next);
                        }
                    }
                    if (elemStack.isEmpty()) {
                        slideBuilder.element(elem.build());
                    } else {
                        elemStack.getLast().element(elem.build());
                    }
                } else {
                    if (!line.endsWith("{")) {
                        throw ParseException.builder()
                                .index(index)
                                .line(line)
                                .message("multi line element start tag can't has content")
                                .build();
                    }
                    elemStack.addLast(elem);
                }
            }
            consumed = true;
        }

        void parseEndTag() {
            if (line.equals("}")) {
                if (elemStack.isEmpty()) {
                    throw ParseException.builder()
                            .index(index)
                            .line(line)
                            .message("no element to close")
                            .build();
                }
                Element elem = elemStack.removeLast().build();
                if (elemStack.isEmpty()) {
                    slideBuilder.element(elem);
                } else {
                    elemStack.getLast().element(elem);
                }
                consumed = true;
            }
        }
    }

    private static Pair<String, String> parseParameter(String line) {
        String[] split = line.split("=", 2);
        String key = split[0].trim();
        String value = "_";
        if (split.length == 2) {
            value = split[1].trim();
        }
        return Pair.of(key, value);
    }
}
