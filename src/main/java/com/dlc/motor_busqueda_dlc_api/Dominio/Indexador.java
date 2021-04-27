package com.dlc.motor_busqueda_dlc_api.Dominio;

import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.IArchivo;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.IDirectorio;

public class Indexador {
    private final Vocabulario vocabulario;
    private final StopWord stopWord;

    public Indexador(Vocabulario vocabulario, StopWord stopWord){
        this.vocabulario = vocabulario;
        this.stopWord = stopWord;
    }

    public void cargarVocabularioArchivo(IArchivo archivo){
        vocabulario.agregarDocumento(archivo.obtenerNombre(), archivo.obtenerPath()); // TODO ver links
        String linea;

        archivo.openReader();
        while( (linea = archivo.obtenerSiguienteLinea()) != null){
            String[] terminos = FormatedorEntrada.formatear(linea);

            for (String termino: terminos) {
                if(termino.equals("")) { continue; }
                if(stopWord.esStopWord(termino)) { continue; }
                vocabulario.agregarTermino(termino, archivo.obtenerNombre());
            }
        }
        archivo.closeReader();
    }

    public void cargarStopWords(IArchivo archivo){
        String linea;

        archivo.openReader();
        while((linea = archivo.obtenerSiguienteLinea()) != null) {
            String[] terminos = FormatedorEntrada.formatear(linea);

            for(String termino: terminos){
                stopWord.agregarStopWord(termino);
            }
        }
        archivo.closeReader();
    }

    public void cargarVocabularioDirectorio(IDirectorio directorio){
        for (IArchivo archivo: directorio.getArchivos()) {
            cargarVocabularioArchivo(archivo);
        }
    }
}
