package cn.xdean.jslide.service.resource;

import java.io.IOException;

public interface Resource {
    String getPath();

    String getContent() throws IOException;

    Resource resolve(String path) throws IOException;
}
