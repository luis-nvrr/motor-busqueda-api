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

        Documento documento = new Documento(nombre, path);
        this.documentos.put(nombre, documento);
    }

    public void agregarTermino(String termino, String documentoString){
        Termino recuperado = terminos.get(termino);
        Documento documento = documentos.get(documentoString);

        if (recuperado == null) { agregarTerminoInexistente(termino, documento); }
        else{
            actualizarTerminoExistente(recuperado, documento);
        }
    }

    private void actualizarTerminoExistente(Termino termino, Documento documento){
        termino.agregarPosteo(documento);
        agregarTerminoAVocabulario(termino.getTerminoAsString(), termino);
    }
    private void agregarTerminoInexistente(String termino, Documento documento){
        Termino nuevo = new Termino(termino);
        nuevo.agregarPosteo(documento);
        agregarTerminoAVocabulario(termino, nuevo);
    }

    private void agregarTerminoAVocabulario(String key, Termino termino){
        this.terminos.put(key, termino);
    }

    public List<Termino> obtenerListaTerminos(String[] terminosString, PosteoRepository posteoRepository) {
        List<Termino> terminosOrdenados = new ArrayList<>();

        for(String terminoString : terminosString){
            Termino termino = this.terminos.get(terminoString);

            if(termino == null) {continue;}

            termino.setPosteos(posteoRepository.getAllPosteosByTermino(terminoString, documentos));
            terminosOrdenados.add(termino);
        }

        terminosOrdenados.sort(Termino::compareTo);
        return terminosOrdenados;
    }

    public void bulkSaveTerminos(TerminoRepository terminoRepository){
        terminoRepository.bulkSaveTerminos(terminos);
    }

    public void bulkSavePosteos(PosteoRepository posteoRepository){
        posteoRepository.bulkSavePosteos(terminos);
    }

    public void bulkSaveDocumentos(DocumentoRepository documentoRepository){
        documentoRepository.bulkSaveDocumentos(this.documentos);
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

    public String getPathDocumento(String documentoString) throws DocumentoNoEncontradoException {
        Documento documento = this.documentos.get(documentoString);

        if(documento == null){
            throw new DocumentoNoEncontradoException();
        }

        return documento.getPath();
    }

    public String mostrarTextoDeDocumento(String documento) throws DocumentoNoEncontradoException {
        Documento buscado = this.documentos.get(documento);
        if(buscado == null){ throw new DocumentoNoEncontradoException();}
        return buscado.getTexto();
    }

    public String[] mostrarNombresDeDocumentos(){
        return documentosToStringArray();
    }

    public String obtenerNombreDelUltimoDocumento(){
        return documentosToStringArray()[0];
    }

    private String[] documentosToStringArray(){
        return this.documentos.keySet().toArray(new String[documentos.size()]);
    }

    public String[] obtenerUltimosTerminosString(){
        return this.terminos.keySet().toArray(new String[terminos.size()]);
    }

    public void actualizarDocumento(String documentoString, DocumentoRepository documentoRepository){
        Documento documentoNuevo = documentoRepository.getDocumento(documentoString);
        this.documentos.put(documentoString, documentoNuevo);
    }

    public void actualizarTerminos(String[] terminosString, TerminoRepository terminoRepository){
        List<Termino> terminosNuevos = terminoRepository.getTerminos(terminosString);
        for (Termino terminoNuevo: terminosNuevos){
            this.terminos.put(terminoNuevo.getTerminoAsString(), terminoNuevo);
        }
    }

    // TODO borrar?
    public void ordenarTerminos(){
        this.terminos = MapUtil.sortByKey(terminos);
    }

    public void ordenarDocumentos(){
        this.documentos = MapUtil.sortByKey(documentos);
    }

    public void ordenarPosteos(){

        for (Map.Entry<String, Termino> entry : terminos.entrySet()) {
            Termino termino = entry.getValue();
            termino.ordenarPosteos();
        }
    }

    private static class MapUtil {
        public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
            List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
            list.sort(Map.Entry.comparingByKey());

            Map<K, V> result = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
        }
    }

    /*
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
     */
}
