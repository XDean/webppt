package cn.xdean.jslide.controller;

import cn.xdean.jslide.model.Slide;
import cn.xdean.jslide.model.SlideSource;
import cn.xdean.jslide.service.ParseService;
import cn.xdean.jslide.service.resource.Resource;
import cn.xdean.jslide.service.resource.ResourceService;
import com.google.common.io.CharStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStreamReader;

@Controller
public class RenderController {

    @Autowired ParseService parseService;
    @Autowired ResourceService resourceService;

    @GetMapping("/parse")
    @ResponseBody
    public Slide parse(@RequestParam("path") String path) throws IOException {
        Resource resource = resourceService.resolve(path).orElseThrow(() -> new IllegalArgumentException("Can't resolve the path"));
        Slide slide = parseService.parse(SlideSource.builder()
                .resource(resource)
                .content(resource.getContent())
                .build());
        return slide;
    }
}
