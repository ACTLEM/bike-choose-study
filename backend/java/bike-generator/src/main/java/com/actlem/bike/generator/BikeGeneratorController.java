package com.actlem.bike.generator;

import com.actlem.commons.model.BikePage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/bikes")
@AllArgsConstructor
public class BikeGeneratorController {

    private final BikeGeneratorService bikeService;

    /**
     * Endpoint to generate a {@link GeneratedBike}
     */
    @PostMapping
    public ResponseEntity<Integer> generate(@RequestBody GenerationConfiguration configuration) {
        return new ResponseEntity<>(bikeService.generate(configuration), HttpStatus.CREATED);
    }

    /**
     * Endpoint to push the {@link GeneratedBike} to an external application.
     * @param size number of bikes per page sent to the application
     */
    @PutMapping
    public ResponseEntity<String> pushBikes(@RequestParam int size) {
        bikeService.pushBikes(size);
        return new ResponseEntity<>("Bikes pushed!", HttpStatus.CREATED);
    }

    /**
     * Endpoint to delete all {@link GeneratedBike}
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        bikeService.deleteAll();
        return new ResponseEntity<>("All bikes have been deleted", HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint to find all {@link GeneratedBike} stored in the repository with a pagination (size=20 by default)
     */
    @GetMapping
    public ResponseEntity<BikePage<GeneratedBike>> findAll(Pageable pageable) {
        return new ResponseEntity<>(bikeService.findAll(pageable), HttpStatus.PARTIAL_CONTENT);
    }
}
