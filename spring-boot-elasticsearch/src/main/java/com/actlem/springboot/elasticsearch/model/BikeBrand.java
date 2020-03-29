package com.actlem.springboot.elasticsearch.model;

import lombok.Getter;

/**
 * Major {@link Bike} brands
 */
public enum BikeBrand {

    BTWIN("Btwin"),
    BIANCHI("Bianchi"),
    BMC("BMC"),
    CANNONDALE("Cannondale"),
    CERVELO("Cervelo"),
    CINELLI("Cinelli"),
    COLNAGO("Colnago"),
    COMMENCAL("Commencal"),
    COWBOY("Cowboy"),
    CUBE("Cube"),
    DIAMONBACK("Diamondback"),
    FELT("Felt"),
    FOCUS("Focus"),
    FUJI("Fuji"),
    GHOST("Ghost"),
    GIANT("Giant"),
    GT("GT"),
    JAMIS("Jamis"),
    KESTREL("Kestrel"),
    KONA("Kona"),
    MERIDA("Merida"),
    NAKAMURA("Nakamura"),
    ORBEA("Orbea"),
    PINARELLO("Pinarello"),
    PRIORITY("Priority"),
    RAD_POWER("Rad Power"),
    RALEIGH("Raleigh"),
    ROSE("Rose"),
    SALSA("Salsa"),
    SANTA_CRUZ("Santa Cruz"),
    SCOTT("Scott"),
    SOMA("Soma"),
    SPECIALIZED("Specialized"),
    SURLY("Surly"),
    TREK("Trek"),
    VAN_RYSEL("Van Rysel");

    @Getter
    private String label;

    BikeBrand(String label) {
        this.label = label;
    }
}
