package com.dlc.motor_busqueda_dlc_api;

import com.dlc.motor_busqueda_dlc_api.Aplicacion.GestorBusqueda;
import com.dlc.motor_busqueda_dlc_api.Dominio.TerminoNoEncontradoException;

public class MainBuscar {

    public static void main(String[] args) {
        GestorBusqueda gestorBusqueda = new GestorBusqueda();

        String terminos = "estonoexiste";

        try {
            gestorBusqueda.buscarTerminos(terminos);
        } catch (TerminoNoEncontradoException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(gestorBusqueda.mostrarCantidadTerminosVocabulario());
    }
}
