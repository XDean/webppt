package cn.xdean.webppt.core.process;

import cn.xdean.webppt.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinuxSudoProcessExecutor implements ProcessExecutor {

    @Autowired AppProperties appProperties;

    @Override
    public Process execute(ProcessBuilder pb) throws IOException {
        List<String> command = new ArrayList<>(pb.command());
        command.addAll(0, Arrays.asList("sudo", "-u", appProperties.getProcess().getSudo()));
        pb.command(command);
        return pb.start();
    }
}
