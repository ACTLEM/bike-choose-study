package com.actlem.url.parameter.generator;

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

class UrlParameterGeneratorControllerTest extends PropertyTest {

    @Mock
    private UrlParameterGeneratorService urlService;

    @InjectMocks
    private UrlParameterGeneratorController cut;

    @RepeatedTest(NUMBER_OF_TESTS)
    @DisplayName("When generating url parameters, then create them in a file via the service")
    void generateCreateUrlParametersViaService(@RandomObject UrlParameterGenerationConfiguration configuration
            , @RandomObject Integer numberOfUrlParameters) throws FileNotFoundException {
        when(urlService.generate(configuration)).thenReturn(numberOfUrlParameters);

        ResponseEntity<Integer> response = cut.generate(configuration);

        assertThat(response).isEqualTo(new ResponseEntity<>(numberOfUrlParameters, HttpStatus.CREATED));
    }
}
