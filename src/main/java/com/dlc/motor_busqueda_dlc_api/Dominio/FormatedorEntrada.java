package com.dlc.motor_busqueda_dlc_api.Dominio;

public class FormatedorEntrada {

    public static String[] formatear(String linea){
        return linea
                .trim()
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .toLowerCase()
                .replaceAll("\\s+", " ")
                .split(" ");
    }
}
