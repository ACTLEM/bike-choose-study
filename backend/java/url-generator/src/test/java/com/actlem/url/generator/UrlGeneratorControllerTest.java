package com.actlem.url.generator;

import com.actlem.commons.junit.extension.PropertyTest;
import com.actlem.commons.junit.extension.RandomParameterExtension.RandomObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UrlGeneratorControllerTest extends PropertyTest {

    @Mock
    private UrlGeneratorService urlService;

    @InjectMocks
    private UrlGeneratorController cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When generating urls, then create them in a file via the service")
    void generateCreateUrlsViaService(@RandomObject UrlGenerationConfiguration configuration, @RandomObject Integer numberOfUrls) throws FileNotFoundException {
        when(urlService.generate(configuration)).thenReturn(numberOfUrls);

        ResponseEntity<Integer> response = cut.generate(configuration);

        assertThat(response).isEqualTo(new ResponseEntity<>(numberOfUrls, HttpStatus.CREATED));
    }
}
