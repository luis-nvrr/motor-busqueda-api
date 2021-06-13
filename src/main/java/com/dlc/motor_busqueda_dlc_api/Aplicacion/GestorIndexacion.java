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
public class GestorIndexacion {

    @Inject
    private Indexador indexador;

    @Inject
    private Vocabulario vocabulario;

    @Inject
    private GestorBusqueda gestorBusqueda;

    public GestorIndexacion(){
    }

    @PostConstruct
    public void init(){
        this.indexador.setVocabulario(vocabulario);
    }

    public void cargarVocabularioDeDirectorio(String directorioPath){
        IDirectorio directorio = new DirectorioLocal(directorioPath);
        this.indexador.cargarVocabularioDeDirectorio(directorio);
        persistirBulk();
    }

    private void persistirBulk(){
        vocabulario.ordenarTerminos();
        vocabulario.ordenarDocumentos();
        vocabulario.ordenarPosteos();
        vocabulario.bulkSaveDocumentos();
        vocabulario.bulkSaveTerminos();
        vocabulario.bulkSavePosteos();
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

    public int mostrarCantidadTerminosVocabulario(){
        return vocabulario.cantidadTerminos();
    }

}
