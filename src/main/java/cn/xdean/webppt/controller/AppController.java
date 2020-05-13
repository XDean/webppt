package cn.xdean.webppt.controller;

import cn.xdean.webppt.controller.model.ElementDTO;
import cn.xdean.webppt.core.model.Element;
import cn.xdean.webppt.core.parse.ParseService;
import cn.xdean.webppt.core.render.RenderService;
import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

@Controller
public class AppController {

    @Autowired ParseService parseService;
    @Autowired ResourceLoader resourceLoader;
    @Autowired RenderService renderService;

    @GetMapping("/parse/")
    @ResponseBody
    public ElementDTO parse(@RequestParam("path") String path) {
        try {
            Resource resource = resourceLoader.getResource(path);
            String content = CharStreams.toString(new InputStreamReader(resource.getInputStream()));
            Element slide = parseService.parse(content);
            return ElementDTO.from(slide);
        } catch (IOException e) {
            throw new IllegalArgumentException("Resource can't be load", e);
        }
    }

    @GetMapping("/render/")
    @ResponseBody
    public String render(@RequestParam("path") String path) {
        try {
            Resource resource = resourceLoader.getResource(path);
            String content = CharStreams.toString(new InputStreamReader(resource.getInputStream()));
            Element slide = parseService.parse(content);
            return renderService.renderElement(resource, slide);
        } catch (IOException e) {
            throw new IllegalArgumentException("Resource can't be load", e);
        }
    }

    @GetMapping("/render/**")
    public ResponseEntity<?> render(HttpServletRequest request,
                                    @RequestHeader(HttpHeaders.REFERER) URL referer) throws IOException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return resolveResource(path.substring("/render/".length()), referer);
    }

    @GetMapping("/resource/")
    public ResponseEntity<?> redirect(@RequestParam("path") String path,
                                      @RequestHeader(HttpHeaders.REFERER) URL referer) throws IOException {
        return resolveResource(path, referer);
    }

    private ResponseEntity<?> resolveResource(String path, URL referer) throws IOException {
        String query = referer.getQuery();
        Map<String, String> params = Splitter.on('&').trimResults().withKeyValueSeparator('=').split(query);
        String originPath = params.get("path");
        if (originPath == null) {
            return ResponseEntity.badRequest().body("referer must have path parameter");
        }
        Resource resource = resourceLoader.getResource(originPath);
        Resource relResource = resource.createRelative(path);
        if (relResource.isFile()) {
            return ResponseEntity.ok(relResource);
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, relResource.getURL().toString()).build();
        }
    }
}
