package com.dlc.motor_busqueda_dlc_api.Aplicacion;

import com.dlc.motor_busqueda_dlc_api.Dominio.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class GestorBusqueda {
    @Inject
    private Vocabulario vocabulario;

    @Inject
    private Buscador buscador;

    public GestorBusqueda(){
    }

    @PostConstruct
    private void init(){
        this.buscador.setVocabulario(vocabulario);
        recuperarVocabulario();
    }

    public void recuperarVocabulario(){
        this.vocabulario.getAllDocumentos();
        this.vocabulario.getAllTerminos();
    }

    public String buscarPathDocumento(String documento) throws DocumentoNoEncontradoException {
        return vocabulario.getPathDocumento(documento);
    }

    public void buscarTerminos(String terminos) throws TerminoNoEncontradoException {
        this.buscador.buscar(terminos);
    }

    public void actualizarVocabularioLocal(String documento, String[] terminos){
        this.vocabulario.actualizarDocumento(documento);
        this.vocabulario.actualizarTerminos(terminos);
    }

    public String[] buscarNombresDocumentos(){
        return this.vocabulario.mostrarNombresDeDocumentos();
    }

    public List<DocumentoRecuperado> getDocumentosRecuperados(){return this.buscador.getDocumentosRanking();}

    public boolean existeDocumento(String nombreDocumento){
        return this.vocabulario.tieneDocumento(nombreDocumento);
    }

    public String mostrarDocumentosRecuperados() {
        return buscador.mostrarDocumentosRecuperados();
    }

    public String mostrarDocumentos(){
        return vocabulario.mostrarDocumentos();
    }
}
