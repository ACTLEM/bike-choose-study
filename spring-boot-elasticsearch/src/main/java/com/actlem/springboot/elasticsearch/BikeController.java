package com.actlem.springboot.elasticsearch;

import com.actlem.springboot.elasticsearch.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Bike> save(@RequestBody Bike bike) {
        return new ResponseEntity<>(bikeService.save(bike), HttpStatus.CREATED);
    }

    /**
     * Endpoint to find {@link Bike} stored in the repository with a pagination (size=20 by default) and optional filters
     */
    @GetMapping
    public ResponseEntity<Page<Bike>> findBy(
            @RequestParam(required = false, defaultValue = "") List<Type> types,
            @RequestParam(required = false, defaultValue = "") List<Gender> genders,
            @RequestParam(required = false, defaultValue = "") List<BikeBrand> brands,
            @RequestParam(required = false, defaultValue = "") List<Material> frames,
            @RequestParam(required = false, defaultValue = "") List<Material> forks,
            @RequestParam(required = false, defaultValue = "") List<Brake> brakes,
            @RequestParam(required = false, defaultValue = "") List<CableRouting> cableRoutings,
            @RequestParam(required = false, defaultValue = "") List<Chainset> chainsets,
            @RequestParam(required = false, defaultValue = "") List<GroupsetBrand> groupsets,
            @RequestParam(required = false, defaultValue = "") List<WheelSize> wheelSizes,
            @RequestParam(required = false, defaultValue = "") List<Color> colors,
            Pageable pageable) {
        FilterList filterList = buildFilters(types, genders, brands, frames, forks, brakes,
                cableRoutings, chainsets, groupsets, wheelSizes, colors);
        return new ResponseEntity<>(bikeService.findBy(pageable, filterList), HttpStatus.PARTIAL_CONTENT);
    }

    /**
     * Endpoint to get possible values to filters {@link Bike} stored in the repository, a.k.a facets.
     */
    @GetMapping("facets")
    public ResponseEntity<List<Facet>> findAllFacets() {
        return new ResponseEntity<>(bikeService.findAllFacets(), HttpStatus.OK);
    }

    private FilterList buildFilters(List<Type> types,
                                    List<Gender> genders,
                                    List<BikeBrand> brands,
                                    List<Material> frames,
                                    List<Material> forks,
                                    List<Brake> brakes,
                                    List<CableRouting> cableRoutings,
                                    List<Chainset> chainsets,
                                    List<GroupsetBrand> groupsets,
                                    List<WheelSize> wheelSizes,
                                    List<Color> colors) {
        return new FilterList().addTypes(types)
                .addGenders(genders)
                .addBikeBrands(brands)
                .addFrameMaterials(frames)
                .addForkMaterials(forks)
                .addBrakes(brakes)
                .addCableRoutings(cableRoutings)
                .addChainsets(chainsets)
                .addGroupsetBrands(groupsets)
                .addWheelSizes(wheelSizes)
                .addColors(colors);
    }
}
