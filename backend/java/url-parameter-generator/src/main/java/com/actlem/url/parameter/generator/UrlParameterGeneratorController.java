package com.actlem.url.parameter.generator;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileNotFoundException;

@Controller
@RequestMapping("/url-parameters")
@AllArgsConstructor
public class UrlParameterGeneratorController {

    private final UrlParameterGeneratorService urlService;

    /**
     * Endpoint to generate URL parameters and return the number of created instances of URL parameters
     */
    @PostMapping
    public ResponseEntity<Integer> generate(@RequestBody UrlParameterGenerationConfiguration configuration) throws FileNotFoundException {
        return new ResponseEntity<>(urlService.generate(configuration), HttpStatus.CREATED);
    }
}
