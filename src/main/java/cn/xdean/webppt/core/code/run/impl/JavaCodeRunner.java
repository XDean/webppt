package cn.xdean.webppt.core.code.run.impl;

import cn.xdean.webppt.core.code.run.CompileCodeRunner;
import org.springframework.stereotype.Component;

@Component
public class JavaCodeRunner extends CompileCodeRunner {
    @Override
    protected String[] compileCommand() {
        return new String[]{"javac", "Main.java"};
    }

    @Override
    protected String[] runCommand() {
        return new String[]{"java", "Main"};
    }

    @Override
    protected String scriptFileName() {
        return "Main.java";
    }
}
