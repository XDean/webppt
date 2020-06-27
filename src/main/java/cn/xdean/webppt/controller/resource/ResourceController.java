package cn.xdean.webppt.controller.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class ResourceController {

    @Autowired ResourceLoader resourceLoader;

    @GetMapping("/api/resource")
    public ResponseEntity<?> redirect(@RequestParam("path") String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        if (resource.isFile()) {
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, resource.getURL().toString())
                    .build();
        }
    }

}
