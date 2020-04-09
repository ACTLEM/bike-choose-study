package com.actlem.springboot.solr;

import com.actlem.commons.model.BikePage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/bikes")
@AllArgsConstructor
public class SolrBikeController {

    private SolrBikeService bikeService;

    /**
     * Endpoint to create a {@link SolrBike}
     */
    @PostMapping
    public ResponseEntity<SolrBike> save(@RequestBody SolrBike bike) {
        return new ResponseEntity<>(bikeService.save(bike), HttpStatus.CREATED);
    }

    /**
     * Endpoint to find {@link SolrBike} stored in the repository with a pagination (size=20 by default)
     */
    @GetMapping
    public ResponseEntity<BikePage<SolrBike>> findAll(Pageable pageable) {
        return new ResponseEntity<>(bikeService.findAll(pageable), HttpStatus.PARTIAL_CONTENT);
    }

}
