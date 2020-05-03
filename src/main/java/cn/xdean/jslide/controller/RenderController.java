package cn.xdean.jslide.controller;

import cn.xdean.jslide.model.Element;
import cn.xdean.jslide.model.SlideSource;
import cn.xdean.jslide.service.ParseService;
import cn.xdean.jslide.service.render.RenderService;
import cn.xdean.jslide.service.resource.Resource;
import cn.xdean.jslide.service.resource.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.function.Function;

@Controller
public class RenderController {

    @Autowired ParseService parseService;
    @Autowired ResourceService resourceService;
    @Autowired RenderService renderService;

    @GetMapping("/parse")
    @ResponseBody
    public Element parse(@RequestParam("path") String path) throws IOException {
        Resource resource = resourceService.resolve(path).orElseThrow(() -> new IllegalArgumentException("Can't resolve the path"));
        Element slide = parseService.parse(SlideSource.builder()
                .resource(resource)
                .content(resource.getContent())
                .build());
        return slide;
    }


    @GetMapping("/render")
    @ResponseBody
    public String render(@RequestParam("path") String path,
                         ModelMap modelMap) throws IOException {
        Resource resource = resourceService.resolve(path).orElseThrow(() -> new IllegalArgumentException("Can't resolve the path"));
        Element slide = parseService.parse(SlideSource.builder()
                .resource(resource)
                .content(resource.getContent())
                .build());
        return renderService.renderElement(slide);
    }
}
