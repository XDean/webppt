package cn.xdean.jslide.service.resource;

import java.io.IOException;

public interface Resource {
    String getAbsPath();

    String getContent() throws IOException;

    Resource resolve(String path) throws IOException;
}
