package cn.xdean.jslide.service.resource;

import java.io.IOException;
import java.util.Optional;

public interface ResourceResolver {
    Optional<Resource> resolve(String path) throws IOException;
}
