package com.dlc.motor_busqueda_dlc_api.Aplicacion;

public class MainCargar {
    public static void main(String[] args) {
        GestorIndexacionDirectorio gestorIndexacion = new GestorIndexacionDirectorio();
        gestorIndexacion.cargarVocabulario(System.getenv("DIRECTORIO_DOCUMENTOS"));
        System.out.println("cantidad de termios: " + gestorIndexacion.mostrarCantidadTerminosVocabulario());
    }
}
