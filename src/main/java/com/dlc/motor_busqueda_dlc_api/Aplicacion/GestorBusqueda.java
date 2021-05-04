package com.dlc.motor_busqueda_dlc_api.Aplicacion;

import com.dlc.motor_busqueda_dlc_api.Dominio.*;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLDocumentoRepository;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLPosteoRepository;
import com.dlc.motor_busqueda_dlc_api.Infraestructura.MySQLTerminoRepository;
import com.dlc.motor_busqueda_dlc_api.Dominio.*;

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

    public String buscarPathDocumento(String documento){
        return vocabulario.getPathDocumento(documento);
    }

    public void buscar(String terminos) throws TerminoNoEncontradoException {
        this.buscador.buscar(terminos);
    }

    public List<DocumentoRecuperado> getDocumentosRecuperados(){return this.buscador.getDocumentosRecuperados();}

    public String mostrarDocumentosRecuperados() {
        return buscador.mostrarDocumentosRecuperados();
    }

    public String mostrarDocumentos(){
        return vocabulario.mostrarDocumentos();
    }

    public int mostrarCantidadTerminosVocabulario(){
        return vocabulario.cantidadTerminos();
    }
}
