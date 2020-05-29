package cn.xdean.webppt.config;

import lombok.Data;

@Data
public class AppProperties {
    Process process = new Process();

    @Data
    public static class Process {
        public enum Type {
            NULL,
            SUDO,
        }

        Type type = Type.NULL;
        String sudo = "nobody";
        String runas = "/trustlevel:0x20000";
    }
}
