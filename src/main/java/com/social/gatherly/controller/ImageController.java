package com.social.gatherly.controller;


import com.social.gatherly.configuration.GlobalConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final GlobalConfig globalConfig;

    @GetMapping("/image/**")
    public ResponseEntity<Resource> getImage(HttpServletRequest request)
        throws IOException {
        String subPath = request.getRequestURI().substring("/image/".length());
        Path file = Paths.get(globalConfig.getImageDir(), subPath);
        System.out.println("Looking for: [" + file.toAbsolutePath() + "] exists=" + java.nio.file.Files.exists(file));
        Resource resource = new UrlResource(file.toUri());
        if(!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = Files.probeContentType(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType != null ? contentType: "application/octet-stream"))
                .body(resource);

    }
}
