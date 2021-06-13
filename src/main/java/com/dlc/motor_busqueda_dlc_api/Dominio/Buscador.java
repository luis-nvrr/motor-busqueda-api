package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Buscador {
    private Vocabulario vocabulario;
    private List<DocumentoRecuperado> documentosRanking;
    //private final PosteoRepository posteoRepository;
    private final int CANTIDAD_DOCUMENTOS = 5;

    public Buscador(){

    }

    public Buscador(Vocabulario vocabulario, PosteoRepository posteoRepository){
        this.vocabulario = vocabulario;
        //this.posteoRepository = posteoRepository;
    }

    public void setVocabulario(Vocabulario vocabulario){
        this.vocabulario = vocabulario;
    }

    public void buscar(String terminos) throws TerminoNoEncontradoException {
        this.documentosRanking = new ArrayList<>();
        String[] terminosSeparados =  FormatedorEntrada.formatear(terminos);
        List<Termino> terminosOrdenados = vocabulario.obtenerListaTerminos(terminosSeparados);

        if(terminosOrdenados.isEmpty()) throw new TerminoNoEncontradoException();

        for(Termino termino: terminosOrdenados){
            agregarAlRankingDocumentosDelTermino(termino);
        }
        ordenarListadoDescendente();
    }

    public List<DocumentoRecuperado> getDocumentosRanking(){return documentosRanking;}

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

            iteracion++;
        }
    }

    private DocumentoRecuperado buscarDocumentoEnElRanking(Documento documento){

        for (DocumentoRecuperado documentoRecuperado : documentosRanking) {
            if (documentoRecuperado.getNombre().equals(documento.getNombre())) {
                return documentoRecuperado;
            }
        }
        return null;
    }

    private DocumentoRecuperado crearNuevoDocumento(Documento documento){
        String nombre = documento.getNombre();
        DocumentoRecuperado documentoRecuperado = new DocumentoRecuperado(nombre);
        this.documentosRanking.add(documentoRecuperado);

        return documentoRecuperado;
    }

    private void ordenarListadoDescendente(){
        this.documentosRanking.sort(DocumentoRecuperado::compareTo);
    }


    public String mostrarDocumentosRecuperados(){
        Iterator<DocumentoRecuperado> it = documentosRanking.iterator();
        StringBuilder stringBuilder = new StringBuilder();

        while(it.hasNext()){
            DocumentoRecuperado documentoRecuperado = it.next();
            stringBuilder.append(documentoRecuperado.toString());
        }
        return stringBuilder.toString();
    }
}
