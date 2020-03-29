package com.actlem.springboot.elasticsearch;

import com.actlem.springboot.elasticsearch.model.Bike;
import com.actlem.springboot.elasticsearch.model.Facet;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/bikes")
@AllArgsConstructor
public class BikeController {

    private BikeService bikeService;

    /**
     * Endpoint to create a {@link Bike}
     */
    @PostMapping
    public ResponseEntity<Bike> save(@RequestBody Bike bike){
        return new ResponseEntity<>(bikeService.save(bike), HttpStatus.CREATED);
    }

    /**
     * Endpoint to get all {@link Bike} stored in the repository with a pagination (size=20 by default)
     */
    @GetMapping
    public ResponseEntity<Page<Bike>> findAll(Pageable pageable){
        return new ResponseEntity<>(bikeService.findAll(pageable), HttpStatus.PARTIAL_CONTENT);
    }

    /**
     * Endpoint to get possible values to filters {@link Bike} stored in the repository, a.k.a facets.
     */
    @GetMapping("facets")
    public ResponseEntity<List<Facet>> findAllFacets(){
        return new ResponseEntity<>(bikeService.findAllFacets(), HttpStatus.OK);
    }
}
