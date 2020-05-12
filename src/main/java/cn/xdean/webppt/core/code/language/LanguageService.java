package cn.xdean.webppt.core.code.language;

import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {
    @Autowired List<CodeLanguage> languages;

    public Optional<CodeLanguage> getLanguageByResource(Resource resource) {
        String filename = resource.getFilename();
        if (filename == null) {
            try {
                String content = CharStreams.toString(new InputStreamReader(resource.getInputStream()));
                return getLanguageByContent(content);
            } catch (IOException e) {
                return Optional.empty();
            }
        } else {
            String ext = Files.getFileExtension(filename);
            if (ext.isEmpty()) {
                return Optional.empty();
            } else {
                return getLanguageByExt(ext);
            }
        }
    }

    public Optional<CodeLanguage> getLanguageByContent(String content) {
        return languages.stream()
                .filter(c -> c.getContentMatch().test(content))
                .findFirst();
    }

    public Optional<CodeLanguage> getLanguageByExt(String ext) {
        return languages.stream()
                .filter(c -> c.getExtensions().contains(ext))
                .findFirst();
    }

    public Optional<CodeLanguage> getLanguageByName(String name) {
        return languages.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}
