package com.actlem.commons.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.actlem.commons.model.Attribute.*;

/**
 * Represents the filters used by the user to filter {@link Bike}
 */
public class FilterList {

    @Getter
    Map<Attribute, List<? extends ReferenceRepository>> filters = new HashMap<>();

    public FilterList addTypes(List<Type> types) {
        if (!types.isEmpty()) {
            filters.put(TYPE, types);
        }
        return this;
    }

    public FilterList addGenders(List<Gender> genders) {
        if (!genders.isEmpty()) {
            filters.put(GENDER, genders);
        }
        return this;
    }

    public FilterList addBikeBrands(List<BikeBrand> bikeBrands) {
        if (!bikeBrands.isEmpty()) {
            filters.put(BRAND, bikeBrands);
        }
        return this;
    }

    public FilterList addFrameMaterials(List<Material> frameMaterials) {
        if (!frameMaterials.isEmpty()) {
            filters.put(FRAME, frameMaterials);
        }
        return this;
    }

    public FilterList addForkMaterials(List<Material> forkMaterials) {
        if (!forkMaterials.isEmpty()) {
            filters.put(FORK, forkMaterials);
        }
        return this;
    }

    public FilterList addBrakes(List<Brake> brakes) {
        if (!brakes.isEmpty()) {
            filters.put(BRAKE, brakes);
        }
        return this;
    }

    public FilterList addCableRoutings(List<CableRouting> cableRoutings) {
        if (!cableRoutings.isEmpty()) {
            filters.put(CABLE_ROUTING, cableRoutings);
        }
        return this;
    }

    public FilterList addChainsets(List<Chainset> chainsets) {
        if (!chainsets.isEmpty()) {
            filters.put(CHAINSET, chainsets);
        }
        return this;
    }

    public FilterList addGroupsetBrands(List<GroupsetBrand> groupsetBrands) {
        if (!groupsetBrands.isEmpty()) {
            filters.put(GROUPSET, groupsetBrands);
        }
        return this;
    }

    public FilterList addWheelSizes(List<WheelSize> wheelSizes) {
        if (!wheelSizes.isEmpty()) {
            filters.put(WHEEL_SIZE, wheelSizes);
        }
        return this;
    }

    public FilterList addColors(List<Color> colors) {
        if (!colors.isEmpty()) {
            filters.put(COLOR, colors);
        }
        return this;
    }

}
