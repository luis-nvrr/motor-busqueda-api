package com.dlc.motor_busqueda_dlc_api;

import com.dlc.motor_busqueda_dlc_api.Aplicacion.GestorBusqueda;
import com.dlc.motor_busqueda_dlc_api.Dominio.TerminoNoEncontradoException;

public class MainBuscar {

    public static void main(String[] args) {
        GestorBusqueda gestorBusqueda = new GestorBusqueda();
        gestorBusqueda.recuperarVocabulario();

        String terminos = "prueba";

        try {
            gestorBusqueda.buscar(terminos);
        } catch (TerminoNoEncontradoException e) {
            e.printStackTrace();
        }

        //System.out.println(gestorBusqueda.mostrarDocumentosRecuperados());
        System.out.println(gestorBusqueda.mostrarDocumentos());
        System.out.println(gestorBusqueda.mostrarCantidadTerminosVocabulario());
    }
}
