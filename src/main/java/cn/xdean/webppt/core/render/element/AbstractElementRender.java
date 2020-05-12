package cn.xdean.webppt.core.render.element;

import cn.xdean.webppt.core.error.AppException;
import cn.xdean.webppt.core.model.Element;
import cn.xdean.webppt.core.model.Parameter;
import cn.xdean.webppt.core.render.ElementRender;
import cn.xdean.webppt.core.render.RenderService;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;

import java.util.List;

public abstract class AbstractElementRender implements ElementRender {

    protected final String name;
    protected @Autowired RenderService renderService;
    protected @Autowired ConversionService conversionService;

    public AbstractElementRender(String name) {
        this.name = name;
    }

    @Override
    public boolean support(String name) {
        return this.name.equals(name);
    }

    protected void assertTopLevel(Element element) {
        if (!element.isDeep(1)) {
            throw AppException.builder()
                    .line(element.getRawInfo().getStartLineIndex())
                    .message(String.format("'%s' must be top level element", element.getName()))
                    .build();
        }
    }

    protected void assertNoChildElement(Element element) {
        List<Element> elements = element.getElements();
        if (!elements.isEmpty()) {
            throw AppException.builder()
                    .line(elements.get(0).getRawInfo().getStartLineIndex())
                    .message(String.format("'%s' can't have child element", element.getName()))
                    .build();
        }
    }

    protected void assertSingleText(Element element) {
        if (element.getTexts().size() != 1) {
            throw AppException.builder()
                    .line(element.getRawInfo().getStartLineIndex())
                    .message(String.format("'%s' can't have multiple text", element.getName()))
                    .build();
        }
    }

    protected void assertNoText(Element element) {
        if (!element.getTexts().isEmpty()) {
            throw AppException.builder()
                    .line(element.getRawInfo().getStartLineIndex())
                    .message(String.format("'%s' can't have text", element.getName()))
                    .build();
        }
    }

    protected void assertParameterFirst(Element element) {
        int parameterCount = (int) (Observable.fromIterable(element.getChildren())
                .takeUntil(n -> !(n instanceof Parameter))
                .count()
                .blockingGet() - 1);
        List<Parameter> parameters = element.getParameters();
        if (parameterCount < parameters.size()) {
            throw AppException.builder()
                    .line(parameters.get(parameterCount).getRawInfo().getStartLineIndex())
                    .message(String.format("'%s''s parameters must defined at beginning", element.getName()))
                    .build();
        }
    }

    protected String resolveParameter(Element element, String key) {
        return resolveParameter(element, String.class, key);
    }

    protected String resolveParameter(Element element, String key, String defaultValue) {
        return resolveParameter(element, String.class, key, defaultValue);
    }

    protected <T> T resolveParameter(Element element, Class<T> clz, String key) {
        return resolveParameter(element, clz, key, null, false);
    }

    protected <T> T resolveParameter(Element element, Class<T> clz, String key, T defaultValue) {
        return resolveParameter(element, clz, key, defaultValue, true);
    }

    private <T> T resolveParameter(Element element, Class<T> clz, String key, T defaultValue, boolean canBeNull) {
        Parameter parameter = element.getParameter(key);
        if (parameter == null) {
            if (canBeNull) {
                return defaultValue;
            } else {
                throw AppException.builder()
                        .line(element.getRawInfo().getStartLineIndex())
                        .message(String.format("'%s' required parameter '%s' not present", element.getName(), key))
                        .build();
            }
        }
        if (conversionService.canConvert(String.class, clz)) {
            try {
                return conversionService.convert(parameter.getValue(), clz);
            } catch (ConversionException e) {
                throw AppException.builder()
                        .line(parameter.getRawInfo().getStartLineIndex())
                        .message(String.format("Fail to parse parameter '%s' to %s", parameter.getValue(), clz))
                        .build();
            }
        } else {
            throw new UnsupportedOperationException(String.format("Can't convert String to %s", clz));
        }
    }
}
