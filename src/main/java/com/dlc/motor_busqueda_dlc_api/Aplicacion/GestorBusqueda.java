package com.dlc.motor_busqueda_dlc_api.Aplicacion;

import com.dlc.motor_busqueda_dlc_api.Dominio.*;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLDocumentoRepository;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLPosteoRepository;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLTerminoRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class GestorBusqueda {
    DocumentoRepository documentoRepository;
    PosteoRepository posteoRepository;
    TerminoRepository terminoRepository;
    Vocabulario vocabulario;
    Buscador buscador;

    public GestorBusqueda(){
        this.documentoRepository = new MySQLDocumentoRepository();
        this.posteoRepository = new MySQLPosteoRepository();
        this.terminoRepository = new MySQLTerminoRepository();
        this.vocabulario = new Vocabulario();
        this.buscador = new Buscador(vocabulario, posteoRepository);
        recuperarVocabulario();
    }

    public void recuperarVocabulario(){
        this.vocabulario.getAllDocumentos(documentoRepository);
        this.vocabulario.getAllTerminos(terminoRepository);
    }

    public String buscarPathDocumento(String documento) throws DocumentoNoEncontradoException {
        return vocabulario.getPathDocumento(documento);
    }

    public void buscarTerminos(String terminos) throws TerminoNoEncontradoException {
        this.buscador.buscar(terminos);
    }

    public void actualizarVocabularioLocal(String documento, String[] terminos){
        vocabulario.actualizarDocumento(documento, documentoRepository);
        vocabulario.actualizarTerminos(terminos, terminoRepository);
    }

    public String[] buscarNombresDocumentos(){
        return this.vocabulario.mostrarNombresDeDocumentos();
    }

    public List<DocumentoRecuperado> getDocumentosRecuperados(){return this.buscador.getDocumentosRanking();}

    public int mostrarCantidadTerminosVocabulario(){
        return vocabulario.cantidadTerminos();
    }

    public boolean existeDocumento(String nombreDocumento){
        return this.vocabulario.tieneDocumento(nombreDocumento);
    }

    /*
    public String mostrarDocumentosRecuperados() {
        return buscador.mostrarDocumentosRecuperados();
    }

    public String mostrarDocumentos(){
        return vocabulario.mostrarDocumentos();
    }
     */
}
