package cn.xdean.webppt.core.code.run.impl;

import cn.xdean.webppt.core.code.run.ScriptCodeRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class PythonCodeRunner extends ScriptCodeRunner {
    @Override
    protected String[] scriptCommand(Path script) {
        return new String[]{"python", "main.py"};
    }

    @Override
    protected String scriptFileName() {
        return "main.py";
    }
}
