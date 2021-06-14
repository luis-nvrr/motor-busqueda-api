package com.dlc.motor_busqueda_dlc_api.Aplicacion;
import com.dlc.motor_busqueda_dlc_api.Dominio.*;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.ArchivoLocal;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.DirectorioLocal;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.IArchivo;
import com.dlc.motor_busqueda_dlc_api.Dominio.Archivo.IDirectorio;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class GestorIndexacionArchivo {

    @Inject
    private Indexador indexador;

    @Inject
    private Vocabulario vocabulario;

    @Inject
    private GestorBusqueda gestorBusqueda;

    public GestorIndexacionArchivo(){
    }

    @PostConstruct
    public void init(){
        this.indexador.setVocabulario(vocabulario);
    }

    public void cargarVocabularioDeArchivo(String archivoPath){
        IArchivo archivo = new ArchivoLocal(archivoPath);
        this.indexador.cargarVocabularioDeArchivo(archivo);
        persistir();
        actualizarVocabulario();
    }

    private void persistir(){
        vocabulario.saveDocumentos();
        vocabulario.saveTerminos();
        vocabulario.savePosteos();
    }

    private void actualizarVocabulario(){
        String documento = vocabulario.obtenerNombreDelPrimerDocumento();
        String[] terminos = vocabulario.obtenerTerminosString();
        this.gestorBusqueda.actualizarVocabularioLocal(documento, terminos);
    }
}
