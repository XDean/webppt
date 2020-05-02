package cn.xdean.jslide.service.resource;

import org.springframework.stereotype.Service;
import org.springframework.util.function.SingletonSupplier;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static xdean.jex.util.lang.ExceptionUtil.uncheck;

@Service
public class FileResourceResolver implements ResourceResolver {

    @Override
    public Optional<Resource> resolve(String path) throws MalformedURLException {
        if (!path.startsWith("file://")) {
            return Optional.empty();
        }
        return Optional.of(new FileResource(path.substring(7)));
    }

    private class FileResource implements Resource {
        private final String filepath;
        private final Path path;
        private final SingletonSupplier<String> content;

        FileResource(String filepath) throws MalformedURLException {
            this.filepath = filepath;
            this.path = Paths.get(filepath);
            this.content = SingletonSupplier.of(() -> uncheck(() -> new String(Files.readAllBytes(path))));
        }

        FileResource(Path path) throws MalformedURLException {
            this.filepath = path.toString();
            this.path = path;
            this.content = SingletonSupplier.of(() -> uncheck(() -> new String(Files.readAllBytes(path))));
        }

        @Override
        public String getAbsPath() {
            return filepath;
        }

        @Override
        public String getContent() {
            return content.obtain();
        }

        @Override
        public Resource resolve(String path) throws MalformedURLException {
            Path target = this.path;
            if (!this.filepath.endsWith("/") && !this.filepath.endsWith("\\")) {
                target = target.resolve("..");
            }
            target = target.resolve(path);
            return new FileResource(target);
        }
    }
}
