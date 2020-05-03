package cn.xdean.jslide.service;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.error.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringTokenizer;
import org.apache.commons.text.matcher.StringMatcherFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;

@Service
public class ParseService {
    public Element parse(String source) {
        return new Parser(source).parse();
    }

    private class Parser {
        final String[] lines;

        int index = -1;
        String line;
        boolean consumed;
        Deque<Element.ElementBuilder> elemStack = new ArrayDeque<>();

        Parser(String source) {
            this.lines = source.split("\\R");
            this.elemStack.addLast(Element.builder().name("root").lineIndex(0));
        }

        Element parse() {
            while (nextLine()) {
                if (!line.isEmpty()) {
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
                        case '/':
                            if (line.startsWith("//")) { // comment
                                consumed = true;
                            }
                            break;
                        case '\\':
                            line = line.substring(1);
                            break;
                    }
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
            if (elemStack.size() > 1) {
                throw ParseException.builder()
                        .index(index)
                        .message("unclosed tag: " + elemStack.getLast().build().getName())
                        .build();
            }
            return elemStack.getLast().build();
        }

        boolean nextLine() {
            if (index == lines.length - 1) {
                return false;
            }
            index++;
            line = lines[index];
            consumed = false;
            return true;
        }


        void parseParameter() {
            Pair<String, String> parameter = ParseService.parseParameter(line.substring(1));
            elemStack.getLast().parameter(parameter.getKey(), parameter.getValue());
            consumed = true;
        }

        void parseComponent() {
            Element.ElementBuilder elem = Element.builder().lineIndex(index);
            int splitIndex = StringUtils.indexOfAny(line, "{ ");
            if (splitIndex == -1) {
                elem.name(line.substring(1));
                elemStack.getLast().element(elem.build());
            } else {
                elem.name(line.substring(1, splitIndex));
                if (line.charAt(splitIndex) == ' ') {
                    StringTokenizer t = new StringTokenizer(line.substring(splitIndex + 1), ' ', '"');
                    while (t.hasNext()) {
                        String next = t.next();
                        if (next.startsWith("@")) {
                            Pair<String, String> parameter = ParseService.parseParameter(next.substring(1));
                            elem.parameter(parameter.getKey(), parameter.getValue());
                        } else {
                            elem.line(next);
                        }
                    }
                    elemStack.getLast().element(elem.build());
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
                if (elemStack.size() == 1) {
                    throw ParseException.builder()
                            .index(index)
                            .line(line)
                            .message("no element to close")
                            .build();
                }
                Element elem = elemStack.removeLast().build();
                elemStack.getLast().element(elem);
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
