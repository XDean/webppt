package cn.xdean.webppt.config;

import cn.xdean.webppt.core.process.LinuxSudoProcessExecutor;
import cn.xdean.webppt.core.process.SimpleProcessExecutor;
import cn.xdean.webppt.core.process.ProcessExecutor;
import cn.xdean.webppt.support.convert.StringToEnumConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Autowired
    StringToEnumConverterFactory stringToEnumConverterFactory;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConfigurationProperties(prefix = "webppt")
    public AppProperties appProperties() {
        return new AppProperties();
    }

    @Bean
    public ProcessExecutor processExecutor() {
        switch (appProperties().getProcess().getType()) {
            case SUDO:
                return new LinuxSudoProcessExecutor();
            case NULL:
            default:
                return new SimpleProcessExecutor();
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(stringToEnumConverterFactory);
    }
}
