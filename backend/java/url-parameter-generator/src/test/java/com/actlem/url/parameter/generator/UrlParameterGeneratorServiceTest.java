package com.actlem.url.parameter.generator;

import com.actlem.commons.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import static com.actlem.commons.model.Attribute.*;
import static com.actlem.commons.model.BikeBrand.*;
import static com.actlem.commons.model.CableRouting.*;
import static com.actlem.commons.model.Gender.*;
import static com.actlem.commons.model.Type.*;
import static com.actlem.commons.model.WheelSize.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlParameterGeneratorServiceTest {

    private static final String ANY_FILENAME = "any_csv_file.csv";

    @Mock
    private CombinationService combinationService;

    @Mock
    private PrintWriter printWriter;

    @Captor
    private ArgumentCaptor<String> urlParameterCaptor;

    @InjectMocks
    private UrlParameterGeneratorService cut;

    @Test
    @DisplayName("When combination service throw error for combination of parameters, then fail")
    void generateCombinationParametersFails() {
        when(combinationService.generateCombinations(Attribute.values().length, 1))
                .thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class,
                () -> cut.generate(new UrlParameterGenerationConfiguration(1, 2, ANY_FILENAME)));
    }

    @Test
    @DisplayName("When generating url parameters with a maximum of one parameter and two values, then returns correct")
    void generateOneParameterTwoValues() throws FileNotFoundException {
        buildTestDataSet();

        // All possible URL parameters according to the test data set
        List<String> expectedUrlParameters = List.of(
                 "genders=BOYS",
                 "genders=GIRLS",
                 "genders=BOYS,WOMEN",
                 "genders=BOYS,MEN",
                 "types=BMX",
                 "types=CYCLOCROSS",
                 "types=KIDS,ELECTRIC",
                 "types=URBAN,ROAD"
        );

        cut = new UrlParameterGeneratorService(combinationService) {
            @Override
            PrintWriter getCSVFile(String filename) {
                return printWriter;
            }
        };

        int nbUrlParameters = cut.generate(new UrlParameterGenerationConfiguration(1, 2, ANY_FILENAME));

        verify(printWriter, times(8)).println(urlParameterCaptor.capture());
        List<String> actualUrlParameters = urlParameterCaptor.getAllValues();
        assertThat(nbUrlParameters).isEqualTo(8);
        assertThat(actualUrlParameters).containsAll(expectedUrlParameters);
    }

    @Test
    @DisplayName("When generating url parameters with a maximum of two parameters and two values, then returns correct")
    void generateTwoParameterTwoValues() throws FileNotFoundException {
        buildTestDataSet();

        // All possible url parameters according to the test data set
        List<String> expectedUrlParameters = List.of(
                 "genders=BOYS",
                 "genders=GIRLS",
                 "genders=BOYS,WOMEN",
                 "genders=BOYS,MEN",
                 "types=BMX",
                 "types=CYCLOCROSS",
                 "types=KIDS,ELECTRIC",
                 "types=URBAN,ROAD",
                 "types=BMX&cableRoutings=EXTERNAL",
                 "types=BMX&cableRoutings=INTERNAL",
                 "types=BMX&cableRoutings=EXTERNAL,INTERNAL",
                 "types=BMX&cableRoutings=INTERNAL,MIX",
                 "types=CYCLOCROSS&cableRoutings=EXTERNAL",
                 "types=CYCLOCROSS&cableRoutings=INTERNAL",
                 "types=CYCLOCROSS&cableRoutings=EXTERNAL,INTERNAL",
                 "types=CYCLOCROSS&cableRoutings=INTERNAL,MIX",
                 "types=KIDS,ELECTRIC&cableRoutings=EXTERNAL",
                 "types=KIDS,ELECTRIC&cableRoutings=EXTERNAL",
                 "types=KIDS,ELECTRIC&cableRoutings=INTERNAL",
                 "types=KIDS,ELECTRIC&cableRoutings=INTERNAL",
                 "types=URBAN,ROAD&cableRoutings=EXTERNAL,INTERNAL",
                 "types=URBAN,ROAD&cableRoutings=EXTERNAL,INTERNAL",
                 "types=URBAN,ROAD&cableRoutings=INTERNAL,MIX",
                 "types=URBAN,ROAD&cableRoutings=INTERNAL,MIX",
                 "brands=BIANCHI&wheelSizes=INCH_16",
                 "brands=BIANCHI&wheelSizes=INCH_26",
                 "brands=BIANCHI&wheelSizes=INCH_12,INCH_20",
                 "brands=BIANCHI&wheelSizes=INCH_27_5,INCH_27_5_PLUS",
                 "brands=DIAMONBACK&wheelSizes=INCH_16",
                 "brands=DIAMONBACK&wheelSizes=INCH_26",
                 "brands=DIAMONBACK&wheelSizes=INCH_12,INCH_20",
                 "brands=DIAMONBACK&wheelSizes=INCH_27_5,INCH_27_5_PLUS",
                 "brands=JAMIS,GHOST&wheelSizes=INCH_16",
                 "brands=JAMIS,GHOST&wheelSizes=INCH_26",
                 "brands=JAMIS,GHOST&wheelSizes=INCH_12,INCH_20",
                 "brands=JAMIS,GHOST&wheelSizes=INCH_27_5,INCH_27_5_PLUS",
                 "brands=PINARELLO,BMC&wheelSizes=INCH_16",
                 "brands=PINARELLO,BMC&wheelSizes=INCH_26",
                 "brands=PINARELLO,BMC&wheelSizes=INCH_12,INCH_20",
                 "brands=PINARELLO,BMC&wheelSizes=INCH_27_5,INCH_27_5_PLUS"
        );

        cut = new UrlParameterGeneratorService(combinationService) {
            @Override
            PrintWriter getCSVFile(String filename) {
                return printWriter;
            }
        };

        int nbUrlParameters = cut.generate(new UrlParameterGenerationConfiguration(2, 2, ANY_FILENAME));

        verify(printWriter, times(40)).println(urlParameterCaptor.capture());
        List<String> actualUrlParameters = urlParameterCaptor.getAllValues();
        assertThat(nbUrlParameters).isEqualTo(40);
        assertThat(actualUrlParameters).containsAll(expectedUrlParameters);
    }

    private void buildTestDataSet() {
        int attributeLength = Attribute.values().length; // 11
        when(combinationService.generateCombinations(attributeLength, 1)).thenReturn(List.of(
                new int[]{GENDER.ordinal()},
                new int[]{TYPE.ordinal()}));
        when(combinationService.generateCombinations(attributeLength, 2)).thenReturn(List.of(
                new int[]{TYPE.ordinal(), CABLE_ROUTING.ordinal()},
                new int[]{BRAND.ordinal(), WHEEL_SIZE.ordinal()}));

        int genderLength = Gender.values().length; // 4
        when(combinationService.generateCombinations(genderLength, 1)).thenReturn(List.of(
                new int[]{BOYS.ordinal()},
                new int[]{GIRLS.ordinal()}));
        when(combinationService.generateCombinations(genderLength, 2)).thenReturn(List.of(
                new int[]{BOYS.ordinal(), WOMEN.ordinal()},
                new int[]{BOYS.ordinal(), MEN.ordinal()}));

        int typeLength = Type.values().length; // 9
        when(combinationService.generateCombinations(typeLength, 1)).thenReturn(List.of(
                new int[]{BMX.ordinal()},
                new int[]{CYCLOCROSS.ordinal()}));
        when(combinationService.generateCombinations(typeLength, 2)).thenReturn(List.of(
                new int[]{KIDS.ordinal(), ELECTRIC.ordinal()},
                new int[]{URBAN.ordinal(), ROAD.ordinal()}));

        int cableRoutingLength = CableRouting.values().length; // 3
        when(combinationService.generateCombinations(cableRoutingLength, 1)).thenReturn(List.of(
                new int[]{EXTERNAL.ordinal()},
                new int[]{INTERNAL.ordinal()}));
        when(combinationService.generateCombinations(cableRoutingLength, 2)).thenReturn(List.of(
                new int[]{EXTERNAL.ordinal(), INTERNAL.ordinal()},
                new int[]{INTERNAL.ordinal(), MIX.ordinal()}));

        int brandLength = BikeBrand.values().length; // 37
        when(combinationService.generateCombinations(brandLength, 1)).thenReturn(List.of(
                new int[]{BIANCHI.ordinal()},
                new int[]{DIAMONBACK.ordinal()}));
        when(combinationService.generateCombinations(brandLength, 2)).thenReturn(List.of(
                new int[]{JAMIS.ordinal(), GHOST.ordinal()},
                new int[]{PINARELLO.ordinal(), BMC.ordinal()}));

        int wheelSizeLength = WheelSize.values().length; // 12
        when(combinationService.generateCombinations(wheelSizeLength, 1)).thenReturn(List.of(
                new int[]{INCH_16.ordinal()},
                new int[]{INCH_26.ordinal()}));
        when(combinationService.generateCombinations(wheelSizeLength, 2)).thenReturn(List.of(
                new int[]{INCH_12.ordinal(), INCH_20.ordinal()},
                new int[]{INCH_27_5.ordinal(), INCH_27_5_PLUS.ordinal()}));
    }

}
