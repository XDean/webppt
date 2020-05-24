package cn.xdean.webppt.core.code.run;

import io.reactivex.schedulers.Schedulers;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

@CommonsLog
public abstract class AbstractCodeRunner implements CodeRunner, InitializingBean {
    protected volatile boolean support = false;

    @Override
    public void afterPropertiesSet() {
        Schedulers.io().scheduleDirect(() -> {
            try {
                support = 0 == new ProcessBuilder()
                        .command(getTouchCommand())
                        .start()
                        .waitFor();
                if (support) {
                    log.info(String.format("Code Runner [%s] is enabled", name()));
                } else {
                    log.info(String.format("Code Runner [%s] is not supported", name()));
                }
            } catch (InterruptedException | IOException e) {
                support = false;
                log.info(String.format("Code Runner [%s] init error", name()), e);
            }
        });
    }

    @Override
    public boolean isSupport() {
        return support;
    }

    protected abstract String[] getTouchCommand();
}
