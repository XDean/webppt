package cn.xdean.webppt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FreeMarkerConfig {
    @Autowired
    public void init(freemarker.template.Configuration c) {
        c.setWhitespaceStripping(true);
    }
}
