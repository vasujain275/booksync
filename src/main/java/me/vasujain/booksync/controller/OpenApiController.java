package me.vasujain.booksync.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping
public class OpenApiController {

    @GetMapping(value = "/openapi.yaml", produces = "text/plain")
    public ResponseEntity<String> getOpenApiYaml() throws IOException {
        Resource resource = new ClassPathResource("static/openapi.yaml"); // Static folder
        return ResponseEntity.ok(new String(Files.readAllBytes(resource.getFile().toPath())));
    }
}
