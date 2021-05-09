package com.dlc.motor_busqueda_dlc_api;

import com.dlc.motor_busqueda_dlc_api.Aplicacion.GestorIndexacion;

public class MainCargar {

    public static void main(String[] args) {
        GestorIndexacion gestorIndexacion = new GestorIndexacion();

        String directorioPath = "C:\\Users\\luis\\Downloads\\pruebas\\";

        gestorIndexacion.cargarVocabularioDeDirectorio(directorioPath);
        //gestorIndexacion.cargarVocabularioArchivo(pathArchivo);

        //System.out.println(gestorIndexacion.mostrarVocabulario());
        System.out.println(gestorIndexacion.mostrarCantidadTerminosVocabulario());
        //System.out.println(gestorVisualizacion.mostrarOrdenPosteo());
    }
}
