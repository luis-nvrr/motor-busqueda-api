package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.*;

public class Vocabulario {
    private Map<String, Termino> terminos;
    private Map<String, Documento> documentos;

    public Vocabulario(){
        this.terminos = new Hashtable<>();
        this.documentos = new Hashtable<>();
    }

    public int cantidadTerminos(){
        return terminos.size();
    }

    private boolean tieneDocumento(String nombre){
        return this.documentos.get(nombre) != null;
    }

    public int getCantidadDocumentos(){
        return this.documentos.size();
    }

    public void agregarDocumento(String nombre, String path){
        if(this.tieneDocumento(nombre)){ return; }

        Documento documento = new Documento(nombre, path); // TODO agregar link
        this.documentos.put(nombre, documento);
    }

    public void agregarTermino(String termino, String documentoString){
        Termino recuperado = terminos.get(termino);
        Documento documento = documentos.get(documentoString);

        if (recuperado == null) { agregarInexistente(termino, documento); }
        else{
            actualizarExistente(recuperado, documento);
        }
    }

    private void actualizarExistente(Termino termino, Documento documento){
        termino.agregarPosteo(documento);
        agregarAVocabulario(termino.getTermino(), termino);
    }
    private void agregarInexistente(String termino, Documento documento){
        Termino nuevo = new Termino(termino);
        nuevo.agregarPosteo(documento);
        agregarAVocabulario(termino, nuevo);
    }

    private void agregarAVocabulario(String key, Termino termino){
        this.terminos.put(key, termino);
    }

    public List<Termino> obtenerListaTerminos(String[] terminosString, PosteoRepository posteoRepository) throws TerminoNoEncontradoException {
        List<Termino> terminosOrdenados = new ArrayList<>();

        for(String terminoString : terminosString){
            Termino termino = this.terminos.get(terminoString);
            termino.setPosteos(posteoRepository.getAllPosteosByTermino(terminoString, documentos));
            terminosOrdenados.add(termino);
        }

        terminosOrdenados.sort(Termino::compareTo);
        return terminosOrdenados;
    }

    public String mostrarTerminos(){
        Iterator<Map.Entry<String, Termino>> it = terminos.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();

        while(it.hasNext()) {
            Map.Entry<String, Termino> entry = it.next();
            stringBuilder.append("termino: ");
            stringBuilder.append(entry.getKey());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public String mostrarOrdenPosteo(){
        Iterator<Map.Entry<String, Termino>> it = terminos.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();

        while(it.hasNext()) {
            Map.Entry<String, Termino> entry = it.next();
            stringBuilder.append(entry.getValue().mostrarOrdenPosteo());
        }

        return stringBuilder.toString();
    }

    public String mostrarDocumentos(){
        Iterator<Map.Entry<String, Documento>> it = documentos.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();

        while(it.hasNext()){
            Map.Entry<String, Documento> entry = it.next();
            Documento documento = entry.getValue();
            stringBuilder.append("documento: ").append(documento.getNombre())
                    .append(" ").append("path: ").append(documento.getPath())
                    .append("\n");
        }

        return stringBuilder.toString();
    }

    public void saveTerminos(TerminoRepository terminoRepository){
        terminoRepository.saveTerminos(terminos);
    }

    public void savePosteos(PosteoRepository posteoRepository){
        posteoRepository.savePosteos(terminos);
    }

    public void saveDocumentos(DocumentoRepository documentoRepository){
        documentoRepository.saveDocumentos(this.documentos);
    }

    public void getAllDocumentos(DocumentoRepository documentoRepository){
        this.documentos = documentoRepository.getAllDocumentos();
    }

    public void getAllTerminos(TerminoRepository terminoRepository){
        this.terminos = terminoRepository.getAllTerminos();
    }

    public String getPathDocumento(String documentoString){
        Documento documento = this.documentos.get(documentoString);
        return documento.getPath();
    }
}
