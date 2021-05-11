package com.dlc.motor_busqueda_dlc_api;

import com.dlc.motor_busqueda_dlc_api.Aplicacion.GestorIndexacion;

public class MainCargar {

    public static void main(String[] args) {
        GestorIndexacion gestorIndexacion = new GestorIndexacion();

        //String stopWordsPath = "C:\\Users\\luis\\code\\motor-busqueda-dlc-api\\src\\main\\resources\\stopWords.txt";
        String directorioPath = "F:\\Documentos\\DocumentosTP1";

        //gestorIndexacion.cargarStopWords(stopWordsPath);
        gestorIndexacion.cargarVocabularioDirectorio(directorioPath);
        //gestorIndexacion.cargarVocabularioArchivo(pathArchivo);

        //System.out.println(gestorIndexacion.mostrarVocabulario());
        System.out.println(gestorIndexacion.mostrarCantidadTerminosVocabulario());
        //System.out.println(gestorVisualizacion.mostrarOrdenPosteo());
    }
}
