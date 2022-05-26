package Visualising;

import java.awt.*;

public enum MyColor {
    SAND,
    DIRT,
    GRASS,
    VERYLOWANIMAL,
    LOWANIMAL,
    ANIMAL,
    HIGHANIMAL,
    VERYHIGHANIMAL,
    DOMINATE;


    public Color toColor() {
        return switch (this) {
            case SAND -> new Color(255, 255, 153); // kanarkowy
            case DIRT -> new Color(123, 63, 0); // czekoladowy
            case GRASS -> new Color(0, 128, 0); // zielony
            case VERYLOWANIMAL -> new Color(128, 0, 0); // rdzawy
            case LOWANIMAL -> new Color(255, 0, 0); // czerwony
            case ANIMAL -> new Color(255, 127, 80); // koralowy
            case HIGHANIMAL -> new Color(217, 77, 169); // majerankowy
            case VERYHIGHANIMAL -> new Color(255, 163, 212); // cukierkowy roz
            case DOMINATE -> new Color(0,0,255); // niebieski
        };
    }




}
