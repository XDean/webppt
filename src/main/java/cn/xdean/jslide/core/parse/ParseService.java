package cn.xdean.jslide.core.parse;

import cn.xdean.jslide.core.error.JSlideException;
import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.model.Node;
import cn.xdean.jslide.core.model.Parameter;
import cn.xdean.jslide.core.model.Text;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringTokenizer;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

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
        Deque<Node> elemStack = new ArrayDeque<>();

        Parser(String source) {
            this.lines = source.split("\\R");
            Element root = new Element();
            root.setName("root");
            root.getRawInfo().setStartLineIndex(0);
            this.elemStack.addLast(root);
        }

        Element parse() {
            while (nextLine()) {
                if (!line.isEmpty()) {
                    switch (line.charAt(0)) {
                        case '.':
                            parseElement();
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
                        throw JSlideException.builder()
                                .line(index)
                                .message("plain text content must be in component")
                                .build();
                    } else {
                        Node node = elemStack.getLast();
                        if (node instanceof Text) {
                            ((Text) node).getLines().add(line);
                            node.getRawInfo().setEndLineIndex(index);
                        } else if (node instanceof Element) {
                            Text newText = new Text();
                            newText.getLines().add(line);
                            newText.setParent((Element) node);
                            newText.getRawInfo().setStartLineIndex(index);
                            newText.getRawInfo().setEndLineIndex(index);
                            ((Element) node).getChildren().add(newText);
                            elemStack.addLast(newText);
                        }
                    }
                }
            }
            if (elemStack.size() > 1) {
                int startLine = elemStack.getLast().getRawInfo().getStartLineIndex();
                throw JSlideException.builder()
                        .line(startLine)
                        .message("unclosed tag")
                        .build();
            }
            return (Element) elemStack.getLast();
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

        void addToLast(Node node) {
            Node last = elemStack.getLast();
            if (last instanceof Text) {
                elemStack.removeLast();
                if (((Text) last).getLines().stream().allMatch(l->l.trim().isEmpty())){
                    Objects.requireNonNull(last.getParent()).getChildren().remove(last);
                }
                addToLast(node);
            } else if (last instanceof Element) {
                ((Element) last).getChildren().add(node);
                node.setParent((Element) last);
            }
        }

        void parseParameter() {
            Parameter parameter = parseParameter(line.substring(1));
            parameter.getRawInfo().setStartLineIndex(index);
            parameter.getRawInfo().setEndLineIndex(index);
            addToLast(parameter);
            consumed = true;
        }

        void parseElement() {
            Element elem = new Element();
            elem.getRawInfo().setStartLineIndex(index);
            addToLast(elem);
            int splitIndex = StringUtils.indexOfAny(line, "{ ");
            if (splitIndex == -1) {
                elem.setName(line.substring(1));
                elem.getRawInfo().setEndLineIndex(index);
            } else {
                elem.setName(line.substring(1, splitIndex));
                if (line.charAt(splitIndex) == ' ') {
                    StringTokenizer t = new StringTokenizer(line.substring(splitIndex + 1), ' ', '"');
                    while (t.hasNext()) {
                        String next = t.next();
                        if (next.startsWith("@")) {
                            Parameter parameter = parseParameter(next.substring(1));
                            parameter.setParent(elem);
                            elem.getChildren().add(parameter);
                        } else {
                            if (!elem.getChildren().isEmpty() && (elem.getChildren().getLast() instanceof Text)) {
                                ((Text) elem.getChildren().getLast()).getLines().add(next);
                            } else {
                                Text text = new Text();
                                text.setParent(elem);
                                text.getRawInfo().setStartLineIndex(index);
                                text.getRawInfo().setEndLineIndex(index);
                                text.getLines().add(next);
                                elem.getChildren().add(text);
                            }
                        }
                    }
                } else {
                    if (!line.endsWith("{")) {
                        throw JSlideException.builder()
                                .line(index)
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
                while (true) {
                    if (elemStack.size() == 1) {
                        throw JSlideException.builder()
                                .line(index)
                                .message("no element to close")
                                .build();
                    }
                    Node node = elemStack.removeLast();
                    if (node instanceof Element) {
                        node.getRawInfo().setEndLineIndex(index);
                        break;
                    }
                }
                consumed = true;
            }
        }

        Parameter parseParameter(String line) {
            String[] keyValue = line.split("=", 2);
            String key = keyValue[0].trim();
            String[] keyElement = key.split("@", 2);
            key = keyElement[0];
            String element = null;
            if (keyElement.length==2){
                element = keyElement[1].trim();
            }
            String value = null;
            if (keyValue.length == 2) {
                value = keyValue[1].trim();
            }
            Parameter parameter = Parameter.builder()
                    .element(element)
                    .key(key)
                    .value(value)
                    .build();
            parameter.getRawInfo().setStartLineIndex(index);
            parameter.getRawInfo().setEndLineIndex(index);
            return parameter;
        }
    }
}
