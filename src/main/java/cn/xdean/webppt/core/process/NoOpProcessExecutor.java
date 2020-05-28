package cn.xdean.webppt.core.process;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NoOpProcessExecutor implements ProcessExecutor {
    @Override
    public Process execute(ProcessBuilder pb) throws IOException {
        return pb.start();
    }
}
