package com.actlem.url.generator;

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
class UrlGeneratorServiceTest {

    private static final String ANY_BIKE_URL = "http://localhost:8080/bikes";
    private static final String ANY_FILENAME = "any_csv_file.csv";

    @Mock
    private CombinationService combinationService;

    @Mock
    private PrintWriter printWriter;

    @Captor
    private ArgumentCaptor<String> urlCaptor;

    @InjectMocks
    private UrlGeneratorService cut;

    @Test
    @DisplayName("When combination service throw error for combination of parameters, then fail")
    void generateCombinationParametersFails() {
        when(combinationService.generateCombinations(Attribute.values().length, 1))
                .thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class,
                () -> cut.generate(new UrlGenerationConfiguration(1, 2, ANY_FILENAME)));
    }

    @Test
    @DisplayName("When generating urls with a maximum of one parameter and two values, then returns correct")
    void generateOneParameterTwoValues() throws FileNotFoundException {
        buildTestDataSet();

        // All possible urls according to the test data set
        List<String> expectedUrls = List.of(
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&genders=BOYS",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&genders=GIRLS",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&genders=BOYS,WOMEN",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&genders=BOYS,MEN",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=BMX",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=CYCLOCROSS",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=KIDS,ELECTRIC",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=URBAN,ROAD"
        );

        cut = new UrlGeneratorService(combinationService){
            @Override
            PrintWriter getCSVFile(String filename) {
                return printWriter;
            }
        };
        cut.setExternalApplicationEndpoint(ANY_BIKE_URL);

        int nbUrls = cut.generate(new UrlGenerationConfiguration(1, 2, ANY_FILENAME));

        verify(printWriter, times(9)).println(urlCaptor.capture());
        List<String> actualUrls = urlCaptor.getAllValues();
        assertThat(nbUrls).isEqualTo(9);
        assertThat(actualUrls).containsAll(expectedUrls);
    }

    @Test
    @DisplayName("When generating urls with a maximum of two parameters and two values, then returns correct")
    void generateTwoParameterTwoValues() throws FileNotFoundException {
        buildTestDataSet();

        // All possible urls according to the test data set
        List<String> expectedUrls = List.of(
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&genders=BOYS",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&genders=GIRLS",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&genders=BOYS,WOMEN",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&genders=BOYS,MEN",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=BMX",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=CYCLOCROSS",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=KIDS,ELECTRIC",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=URBAN,ROAD",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=BMX&cableRoutings=EXTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=BMX&cableRoutings=INTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=BMX&cableRoutings=EXTERNAL,INTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=BMX&cableRoutings=INTERNAL,MIX",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=CYCLOCROSS&cableRoutings=EXTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=CYCLOCROSS&cableRoutings=INTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=CYCLOCROSS&cableRoutings=EXTERNAL,INTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=CYCLOCROSS&cableRoutings=INTERNAL,MIX",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=KIDS,ELECTRIC&cableRoutings=EXTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=KIDS,ELECTRIC&cableRoutings=EXTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=KIDS,ELECTRIC&cableRoutings=INTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=KIDS,ELECTRIC&cableRoutings=INTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=URBAN,ROAD&cableRoutings=EXTERNAL,INTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=URBAN,ROAD&cableRoutings=EXTERNAL,INTERNAL",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=URBAN,ROAD&cableRoutings=INTERNAL,MIX",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&types=URBAN,ROAD&cableRoutings=INTERNAL,MIX",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=BIANCHI&wheelSizes=INCH_16",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=BIANCHI&wheelSizes=INCH_26",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=BIANCHI&wheelSizes=INCH_12,INCH_20",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=BIANCHI&wheelSizes=INCH_27_5,INCH_27_5_PLUS",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=DIAMONBACK&wheelSizes=INCH_16",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=DIAMONBACK&wheelSizes=INCH_26",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=DIAMONBACK&wheelSizes=INCH_12,INCH_20",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=DIAMONBACK&wheelSizes=INCH_27_5,INCH_27_5_PLUS",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=JAMIS,GHOST&wheelSizes=INCH_16",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=JAMIS,GHOST&wheelSizes=INCH_26",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=JAMIS,GHOST&wheelSizes=INCH_12,INCH_20",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=JAMIS,GHOST&wheelSizes=INCH_27_5,INCH_27_5_PLUS",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=PINARELLO,BMC&wheelSizes=INCH_16",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=PINARELLO,BMC&wheelSizes=INCH_26",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=PINARELLO,BMC&wheelSizes=INCH_12,INCH_20",
                ANY_BIKE_URL + "?page=${page_number}&size=${page_size}" + "&brands=PINARELLO,BMC&wheelSizes=INCH_27_5,INCH_27_5_PLUS"
        );

        cut = new UrlGeneratorService(combinationService){
            @Override
            PrintWriter getCSVFile(String filename) {
                return printWriter;
            }
        };
        cut.setExternalApplicationEndpoint(ANY_BIKE_URL);

        int nbUrls = cut.generate(new UrlGenerationConfiguration(2, 2, ANY_FILENAME));

        verify(printWriter, times(41)).println(urlCaptor.capture());
        List<String> actualUrls = urlCaptor.getAllValues();
        assertThat(nbUrls).isEqualTo(41);
        assertThat(actualUrls).containsAll(expectedUrls);
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
