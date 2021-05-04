package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Buscador {
    private Vocabulario vocabulario;
    private List<DocumentoRecuperado> documentosRecuperados;
    private PosteoRepository posteoRepository;
    private int CANTIDAD_DOCUMENTOS = 5;

    public Buscador(Vocabulario vocabulario, PosteoRepository posteoRepository){
        this.vocabulario = vocabulario;
        this.posteoRepository = posteoRepository;
    }

    public void buscar(String terminos){
        this.documentosRecuperados = new ArrayList<>();
        String terminosSeparados[] =  FormatedorEntrada.formatear(terminos);
        List<Termino> terminosOrdenados = vocabulario.obtenerListaTerminos(terminosSeparados,posteoRepository);

        for(Termino termino: terminosOrdenados){
            agregarAlRankingDocumentosDelTermino(termino);
        }
        ordenarListadoDescendente();
    }



    public List<DocumentoRecuperado> getDocumentosRecuperados(){return documentosRecuperados;}

    private void agregarAlRankingDocumentosDelTermino(Termino termino){
        List<Posteo> posteos = termino.getPosteos();
        int iteracion = 0;
        Iterator<Posteo> it = posteos.iterator();

        while(it.hasNext() && iteracion < CANTIDAD_DOCUMENTOS){
            Posteo posteo = it.next();
            Documento documento = posteo.getDocumento();
            DocumentoRecuperado documentoRecuperado = buscarDocumentoEnElRanking(documento);

            if(documentoRecuperado == null){ documentoRecuperado = crearNuevoDocumento(documento); }

            double indiceRelevancia = (double) posteo.getFrecuenciaTermino() *
                    (Math.log10((double)vocabulario.getCantidadDocumentos() / (double)termino.getCantidadDocumentos()));
            documentoRecuperado.sumarRelevancia(indiceRelevancia);
        }
    }

    private DocumentoRecuperado buscarDocumentoEnElRanking(Documento documento){

        for (DocumentoRecuperado documentoRecuperado : documentosRecuperados) {
            if (documentoRecuperado.equals(documento.getNombre())) {
                return documentoRecuperado;
            }
        }
        return null;
    }

    private DocumentoRecuperado crearNuevoDocumento(Documento documento){
        String path = documento.getPath();
        String nombre = documento.getNombre();
        DocumentoRecuperado documentoRecuperado = new DocumentoRecuperado(path, nombre);
        this.documentosRecuperados.add(documentoRecuperado);

        return documentoRecuperado;
    }

    private void ordenarListadoDescendente(){
        this.documentosRecuperados.sort(DocumentoRecuperado::compareTo);
    }

    public String mostrarDocumentosRecuperados(){
        Iterator<DocumentoRecuperado> it = documentosRecuperados.iterator();
        StringBuilder stringBuilder = new StringBuilder();

        while(it.hasNext()){
            DocumentoRecuperado documentoRecuperado = it.next();
            stringBuilder.append(documentoRecuperado.toString());
        }
        return stringBuilder.toString();
    }
}
