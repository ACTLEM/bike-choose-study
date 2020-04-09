package com.actlem.springboot.solr;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import com.actlem.commons.model.BikePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class SolrBikeServiceTest extends PropertyTest {

    @Mock
    private SolrBikeRepository bikeRepository;

    @Mock
    private Pageable pageable;

    @Mock
    private Page<SolrBike> page;

    @InjectMocks
    private SolrBikeService cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen saving bike, then create it in the repository")
    void saveCreateBikeInRepository(@RandomObject SolrBike bike) {
        when(bikeRepository.save(bike)).thenReturn(bike);

        SolrBike response = cut.save(bike);

        assertThat(response).isEqualTo(bike);
    }

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("Wen requesting all bikes, then find all from the repository")
    void findAllReturnsBikesFromRepository(@RandomObject BikePage<SolrBike> bikePage) {
        mockPageFromBikePage(page, bikePage);
        when(bikeRepository.findAll(pageable)).thenReturn(page);

        BikePage<SolrBike> response = cut.findAll(pageable);

        assertThat(response).isEqualTo(bikePage);
    }

    private void mockPageFromBikePage(Page<SolrBike> page, BikePage<SolrBike> bikePage) {
        when(page.getContent()).thenReturn(bikePage.getBikes());
        when(page.getNumber()).thenReturn(bikePage.getPageNumber());
        when(page.getSize()).thenReturn(bikePage.getPageSize());
        when(page.getTotalPages()).thenReturn(bikePage.getTotalPages());
        when(page.getNumberOfElements()).thenReturn(bikePage.getNumberOfElements());
        when(page.getTotalElements()).thenReturn(bikePage.getTotalElements());
    }

}
