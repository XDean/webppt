package cn.xdean.jslide.service.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.function.SingletonSupplier;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

@Service
public class HttpResourceResolver implements ResourceResolver {

    @Autowired RestTemplate restTemplate;

    @Override
    public Optional<Resource> resolve(String path) throws MalformedURLException {
        if (!path.startsWith("http://") && !path.startsWith("https://")) {
            return Optional.empty();
        }
        return Optional.of(new HttpResource(path));
    }

    private class HttpResource implements Resource {
        private final String path;
        private final URL url;
        private final SingletonSupplier<String> content;

        HttpResource(String path) throws MalformedURLException {
            this.path = path;
            this.url = new URL(path);
            this.content = SingletonSupplier.of(() -> restTemplate.getForObject(path, String.class));
        }

        HttpResource(URL url) {
            this.path = url.toString();
            this.url = url;
            this.content = SingletonSupplier.of(() -> restTemplate.getForObject(path, String.class));
        }

        @Override
        public String getPath() {
            return path;
        }

        @Override
        public String getContent() {
            return content.obtain();
        }

        @Override
        public Resource resolve(String path) throws MalformedURLException {
            return new HttpResource(new URL(url, path));
        }
    }
}
