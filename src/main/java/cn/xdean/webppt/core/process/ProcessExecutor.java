package cn.xdean.webppt.core.process;

import java.io.IOException;

public interface ProcessExecutor {
    Process execute(ProcessBuilder pb) throws IOException;
}
