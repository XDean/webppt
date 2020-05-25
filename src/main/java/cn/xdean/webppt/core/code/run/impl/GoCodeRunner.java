package cn.xdean.webppt.core.code.run.impl;

import cn.xdean.webppt.core.code.run.CompileCodeRunner;
import cn.xdean.webppt.core.code.run.ScriptCodeRunner;
import org.springframework.stereotype.Component;

@Component
public class GoCodeRunner extends ScriptCodeRunner {

    @Override
    public String name() {
        return "go";
    }

    @Override
    protected String[] getTouchCommand() {
        return new String[]{"go", "version"};
    }

    @Override
    protected String[] scriptCommand() {
        return new String[]{"go", "run", "main.go"};
    }

    @Override
    protected String scriptFileName() {
        return "main.go";
    }

}
