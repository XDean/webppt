package cn.xdean.webppt.core.process;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WindowsRunAsProcessExecutor implements ProcessExecutor {
    @Override
    public Process execute(ProcessBuilder pb) throws IOException {
        List<String> command = new ArrayList<>(pb.command());
        command.addAll(0, Arrays.asList("runas", "/trustlevel:0x20000"));
        pb.command(command);
        return pb.start();
    }
}
