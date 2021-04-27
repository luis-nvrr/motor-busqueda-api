package com.dlc.motor_busqueda_dlc_api.Dominio.Archivo;

public interface IArchivo {

    void openReader();
    String obtenerSiguienteLinea();
    void closeReader();
    String obtenerPath();
    String obtenerNombre();
}
