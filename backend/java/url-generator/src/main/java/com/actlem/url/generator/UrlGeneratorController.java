package com.actlem.url.generator;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;

@Controller
@RequestMapping("/urls")
@AllArgsConstructor
public class UrlGeneratorController {

    private final UrlGeneratorService urlService;

    /**
     * Endpoint to generate urls
     */
    @PostMapping
    public ResponseEntity<Integer> generate(@RequestBody UrlGenerationConfiguration configuration) throws FileNotFoundException {
        return new ResponseEntity<>(urlService.generate(configuration), HttpStatus.CREATED);
    }
}
