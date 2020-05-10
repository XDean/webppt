package cn.xdean.jslide.core.play.impl;

import cn.xdean.jslide.core.play.ScriptCodePlayer;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class PythonCodePlayer extends ScriptCodePlayer {
    public PythonCodePlayer() {
        super("python", "py");
    }

    @Override
    protected String[] scriptCommand(Path script) {
        return new String[]{"python", "main.py"};
    }

    @Override
    protected String scriptFileName() {
        return "main.py";
    }
}
