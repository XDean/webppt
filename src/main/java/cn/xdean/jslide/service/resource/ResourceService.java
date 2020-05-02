package cn.xdean.jslide.service.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ResourceService {

    @Autowired List<ResourceResolver> resolvers = Collections.emptyList();

    public Optional<Resource> resolve(String location) {
        for (ResourceResolver resolver : resolvers) {
            try {
                Optional<Resource> resolve = resolver.resolve(location);
                if (resolve.isPresent()) {
                    return resolve;
                }
            } catch (IOException e) {
                log.warn("resolve resource error: " + resolver, e);
            }
        }
        return Optional.empty();
    }
}
