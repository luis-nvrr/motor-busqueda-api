package com.dlc.motor_busqueda_dlc_api;

import com.dlc.motor_busqueda_dlc_api.Aplicacion.GestorIndexacion;

public class MainCargar {

    public static void main(String[] args) {
        GestorIndexacion gestorIndexacion = new GestorIndexacion();
        String directorioPath = System.getenv("DIRECTORIO_DOCUMENTOS");

        gestorIndexacion.cargarVocabularioDeDirectorio(directorioPath);
        System.out.println(gestorIndexacion.mostrarCantidadTerminosVocabulario());
    }
}
