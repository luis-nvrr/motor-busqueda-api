package com.dlc.motor_busqueda_dlc_api;

import com.dlc.motor_busqueda_dlc_api.Aplicacion.GestorBusqueda;
import com.dlc.motor_busqueda_dlc_api.Dominio.TerminoNoEncontradoException;

public class MainBuscar {

    public static void main(String[] args) {
        GestorBusqueda gestorBusqueda = new GestorBusqueda();

        String terminos = "quixote";
        System.out.println(System.getenv("DIRECTORIO_DOCUMENTOS"));

        try {
            gestorBusqueda.buscarTerminos(terminos);
            System.out.println(gestorBusqueda.mostrarDocumentosRecuperados());
        } catch (TerminoNoEncontradoException e) {
            e.printStackTrace();
        }

    }
}
