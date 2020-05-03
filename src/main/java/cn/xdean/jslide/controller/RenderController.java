package cn.xdean.jslide.controller;

import cn.xdean.jslide.core.model.Element;
import cn.xdean.jslide.core.parse.ParseService;
import cn.xdean.jslide.core.render.RenderService;
import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

@Controller
public class RenderController {

    @Autowired ParseService parseService;
    @Autowired ResourceLoader resourceLoader;
    @Autowired RenderService renderService;

    @GetMapping("/parse/")
    @ResponseBody
    public Element parse(@RequestParam("path") String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        String content = CharStreams.toString(new InputStreamReader(resource.getInputStream()));
        Element slide = parseService.parse(content);
        return slide;
    }

    @GetMapping("/render/")
    @ResponseBody
    public String render(@RequestParam("path") String path,
                         ModelMap modelMap) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        String content = CharStreams.toString(new InputStreamReader(resource.getInputStream()));
        Element slide = parseService.parse(content);
        return renderService.renderElement(slide);
    }

    @GetMapping("/render/{path}")
    public ResponseEntity<?> render(@PathVariable("path") String path,
                                    @RequestHeader(HttpHeaders.REFERER) URL referer,
                                    ModelMap modelMap) throws IOException {
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
