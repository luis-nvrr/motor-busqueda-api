package com.dlc.motor_busqueda_dlc_api.Dominio;

import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.IArchivo;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.IDirectorio;

public class Indexador {
    private Vocabulario vocabulario;

    public Indexador(){

    }

    public void setVocabulario(Vocabulario vocabulario){
        this.vocabulario = vocabulario;
    }

    public void cargarVocabularioDeArchivo(IArchivo archivo){
        vocabulario.agregarDocumento(archivo.obtenerNombre(), archivo.obtenerPath());
        String linea;

        archivo.openReader();
        while( (linea = archivo.obtenerSiguienteLinea()) != null){
            String[] terminos = FormatedorEntrada.formatear(linea);

            for (String termino: terminos) {
                if(termino.equals("")) { continue; }
                vocabulario.agregarTermino(termino, archivo.obtenerNombre());
            }
        }
        archivo.closeReader();
    }

    public void cargarVocabularioDeDirectorio(IDirectorio directorio){
        for (IArchivo archivo: directorio.getArchivos()) {
            cargarVocabularioDeArchivo(archivo);
        }
    }
}
