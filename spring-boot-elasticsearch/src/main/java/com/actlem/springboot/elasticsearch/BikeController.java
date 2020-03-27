package com.actlem.springboot.elasticsearch;

import com.actlem.springboot.elasticsearch.model.Bike;
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


@Controller
@RequestMapping("/bikes")
@AllArgsConstructor
public class BikeController {

    private BikeRepository bikeRepository;

    @PostMapping
    public ResponseEntity<Bike> save(@RequestBody Bike bike){
        return new ResponseEntity<>(bikeRepository.save(bike), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<Bike>> findAll(Pageable pageable){
        return new ResponseEntity<>(bikeRepository.findAll(pageable), HttpStatus.PARTIAL_CONTENT);
    }
}
