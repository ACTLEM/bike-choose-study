package com.actlem.springboot.elasticsearch;

import com.actlem.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.springboot.elasticsearch.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static com.actlem.springboot.elasticsearch.model.Attribute.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class BikeControllerTest extends PropertyTest {

    @Mock
    private BikeService bikeService;

    @Mock
    private Pageable pageable;

    @Captor
    private ArgumentCaptor<FilterList> filterListCaptor;

    @InjectMocks
    private BikeController cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen saving bike, then create it in the repository via the service")
    void saveCreateBikeViaService(@RandomObject Bike bike) {
        when(bikeService.save(bike)).thenReturn(bike);

        ResponseEntity<Bike> response = cut.save(bike);

        assertThat(response).isEqualTo(new ResponseEntity<>(bike, HttpStatus.CREATED));
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting bikes, then find them via the service")
    void findByReturnsBikeFromService(
            @RandomObject BikePage bikePage,
            @RandomObject List<Type> types,
            @RandomObject List<Gender> genders,
            @RandomObject List<BikeBrand> bikeBrands,
            @RandomObject List<Material> frameMaterials,
            @RandomObject List<Material> forkMaterials,
            @RandomObject List<Brake> brakes,
            @RandomObject List<CableRouting> cableRoutings,
            @RandomObject List<Chainset> chainsets,
            @RandomObject List<GroupsetBrand> groupsetBrands,
            @RandomObject List<WheelSize> wheelSizes,
            @RandomObject List<Color> colors
    ) {
        when(bikeService.findBy(eq(pageable), filterListCaptor.capture())).thenReturn(bikePage);

        ResponseEntity<BikePage> response = cut.findBy(types, genders, bikeBrands, frameMaterials, forkMaterials,
                brakes, cableRoutings, chainsets, groupsetBrands, wheelSizes, colors, pageable);

        assertThat(response).isEqualTo(new ResponseEntity<>(bikePage, HttpStatus.PARTIAL_CONTENT));
        assertFilters(types, genders, bikeBrands, frameMaterials, forkMaterials, brakes, cableRoutings, chainsets,
                groupsetBrands, wheelSizes, colors);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting facets, then find the facets via the service")
    void findFacetsReturnsFromService(
            @RandomObject List<Facet> facets,
            @RandomObject List<Type> types,
            @RandomObject List<Gender> genders,
            @RandomObject List<BikeBrand> bikeBrands,
            @RandomObject List<Material> frameMaterials,
            @RandomObject List<Material> forkMaterials,
            @RandomObject List<Brake> brakes,
            @RandomObject List<CableRouting> cableRoutings,
            @RandomObject List<Chainset> chainsets,
            @RandomObject List<GroupsetBrand> groupsetBrands,
            @RandomObject List<WheelSize> wheelSizes,
            @RandomObject List<Color> colors
    ) {
        when(bikeService.findFacets(filterListCaptor.capture())).thenReturn(facets);

        ResponseEntity<List<Facet>> response = cut.findFacets(types, genders, bikeBrands, frameMaterials, forkMaterials,
                brakes, cableRoutings, chainsets, groupsetBrands, wheelSizes, colors);

        assertThat(response).isEqualTo(new ResponseEntity<>(facets, HttpStatus.OK));
        assertFilters(types, genders, bikeBrands, frameMaterials, forkMaterials, brakes, cableRoutings, chainsets,
                groupsetBrands, wheelSizes, colors);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen searching bikes, then search the bikes and facets via the service")
    void searchBikesReturnsFromService(
            @RandomObject BikePage bikePage,
            @RandomObject List<Facet> facets,
            @RandomObject List<Type> types,
            @RandomObject List<Gender> genders,
            @RandomObject List<BikeBrand> bikeBrands,
            @RandomObject List<Material> frameMaterials,
            @RandomObject List<Material> forkMaterials,
            @RandomObject List<Brake> brakes,
            @RandomObject List<CableRouting> cableRoutings,
            @RandomObject List<Chainset> chainsets,
            @RandomObject List<GroupsetBrand> groupsetBrands,
            @RandomObject List<WheelSize> wheelSizes,
            @RandomObject List<Color> colors
    ) {
        SearchResult searchResult = new SearchResult(bikePage, facets);
        when(bikeService.search(eq(pageable), filterListCaptor.capture())).thenReturn(searchResult);

        ResponseEntity<SearchResult> response = cut.search(types, genders, bikeBrands, frameMaterials, forkMaterials,
                brakes, cableRoutings, chainsets, groupsetBrands, wheelSizes, colors, pageable);

        assertThat(response).isEqualTo(new ResponseEntity<>(searchResult, HttpStatus.PARTIAL_CONTENT));
        assertFilters(types, genders, bikeBrands, frameMaterials, forkMaterials, brakes, cableRoutings, chainsets,
                groupsetBrands, wheelSizes, colors);
    }

    private void assertFilters(
            List<Type> types,
            List<Gender> genders,
            List<BikeBrand> bikeBrands,
            List<Material> frameMaterials,
            List<Material> forkMaterials,
            List<Brake> brakes,
            List<CableRouting> cableRoutings,
            List<Chainset> chainsets,
            List<GroupsetBrand> groupsetBrands,
            List<WheelSize> wheelSizes,
            List<Color> colors) {
        Map<Attribute, List<? extends ReferenceRepository>> filters = filterListCaptor.getValue().getFilters();
        assertThat(filters.get(TYPE)).isEqualTo(types.isEmpty() ? null : types);
        assertThat(filters.get(GENDER)).isEqualTo(genders.isEmpty() ? null : genders);
        assertThat(filters.get(BRAND)).isEqualTo(bikeBrands.isEmpty() ? null : bikeBrands);
        assertThat(filters.get(FRAME)).isEqualTo(frameMaterials.isEmpty() ? null : frameMaterials);
        assertThat(filters.get(FORK)).isEqualTo(forkMaterials.isEmpty() ? null : forkMaterials);
        assertThat(filters.get(BRAKE)).isEqualTo(brakes.isEmpty() ? null : brakes);
        assertThat(filters.get(CABLE_ROUTING)).isEqualTo(cableRoutings.isEmpty() ? null : cableRoutings);
        assertThat(filters.get(CHAINSET)).isEqualTo(chainsets.isEmpty() ? null : chainsets);
        assertThat(filters.get(GROUPSET)).isEqualTo(groupsetBrands.isEmpty() ? null : groupsetBrands);
        assertThat(filters.get(WHEEL_SIZE)).isEqualTo(wheelSizes.isEmpty() ? null : wheelSizes);
        assertThat(filters.get(COLOR)).isEqualTo(colors.isEmpty() ? null : colors);
    }

}
