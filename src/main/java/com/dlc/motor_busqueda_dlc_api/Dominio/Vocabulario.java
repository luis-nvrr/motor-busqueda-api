package com.dlc.motor_busqueda_dlc_api.Dominio;

import javax.inject.Inject;
import java.util.*;

public class Vocabulario {
    private Map<String, Termino> terminos;
    private Map<String, Documento> documentos;

    @Inject
    PosteoRepository posteoRepository;

    @Inject
    DocumentoRepository documentoRepository;

    @Inject
    TerminoRepository terminoRepository;

    public Vocabulario(){
        this.terminos = new HashMap<>();
        this.documentos = new HashMap<>();
    }

    // cantidad de terminos en el vocabulario
    public int cantidadTerminos(){
        return terminos.size();
    }

    // retorna true si ya existe un documento en el vocabulario
    public boolean tieneDocumento(String nombre){
        return this.documentos.get(nombre) != null;
    }

    // retorna la cantida de documentos del vocabulario
    public int getCantidadDocumentos(){
        return this.documentos.size();
    }

    // agrega un documento inexistente a la tabla de documentos
    public void agregarDocumento(String nombre, String path){
        if(this.tieneDocumento(nombre)){ return; }

        Documento documento = new Documento(nombre, path);
        this.documentos.put(nombre, documento);
    }

    // agrega un Termino al vocabulario
    // si ya existia, actualiza la lista de posteos
    // si no existe, crea el nuevo Termino
    public void agregarTermino(String termino, String documentoString){
        Termino recuperado = terminos.get(termino);
        Documento documento = documentos.get(documentoString);

        if (recuperado == null) {
            recuperado = new Termino(termino);
            this.terminos.put(termino, recuperado);
        }
        recuperado.agregarPosteo(documento);
    }

    // retorna la lista de objetos Terminos buscados
    public List<Termino> obtenerListaTerminos(String[] terminosString) {
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

    // guarda la tabla de terminos, indexados de un directorio
    public void bulkSaveTerminos(TerminoRepository terminoRepository){
        terminoRepository.bulkSaveTerminos(terminos);
    }

    // guarda la lista de posteos de cada termino, indexado de un directorio
    public void bulkSavePosteos(PosteoRepository posteoRepository){
        posteoRepository.bulkSavePosteos(terminos);
    }

    // gurda la tabla de documentos, indexados de un directorio
    public void bulkSaveDocumentos(DocumentoRepository documentoRepository){
        documentoRepository.bulkSaveDocumentos(this.documentos);
    }

    // guarda la tabla de terminos, indexado de un archivo
    public void saveTerminos(){
        terminoRepository.saveTerminos(terminos);
    }

    // guarda la lista de posteos de cada termino, indexado de un archivo
    public void savePosteos(){
        posteoRepository.savePosteos(terminos);
    }

    // guarda la tabla de documentos
    public void saveDocumentos(){
        documentoRepository.saveDocumentos(this.documentos);
    }

    // recupera la tabla de documentos
    public void getAllDocumentos(){
        this.documentos = documentoRepository.getAllDocumentos();
    }

    // recupera la tabla de terminos
    public void getAllTerminos(){
        this.terminos = terminoRepository.getAllTerminos();
    }

    // recupera la ubicacion de un archivo pasado como parametro
    public String getPathDocumento(String documentoString) throws DocumentoNoEncontradoException {
        Documento documento = this.documentos.get(documentoString);

        if(documento == null){
            throw new DocumentoNoEncontradoException();
        }

        return documento.getPath();
    }

    // recupera los nombres de los documentos en la tabla de documentos
    public String[] mostrarNombresDeDocumentos(){
        return documentosToStringArray();
    }

    // recupera el primer documento de la tabla de documentos
    public String obtenerNombreDelPrimerDocumento(){
        return documentosToStringArray()[0];
    }

    // convierte la tabla de documentos en una lista de String con los nombres
    private String[] documentosToStringArray(){
        return this.documentos.keySet().toArray(new String[documentos.size()]);
    }

    public String[] obtenerTerminosString(){
        return this.terminos.keySet().toArray(new String[terminos.size()]);
    }

    // busca en la BD un documento por nombre y lo actualiza en la Hashtable
    public void actualizarDocumento(String documentoString){
        Documento documentoNuevo = documentoRepository.getDocumento(documentoString);
        this.documentos.put(documentoString, documentoNuevo);
    }

    // busca en la BD un termino y lo actualiza en la Hashtable
    public void actualizarTerminos(String[] terminosString){
        List<Termino> terminosNuevos = terminoRepository.getTerminos(terminosString);
        for (Termino terminoNuevo: terminosNuevos){
            this.terminos.put(terminoNuevo.getTerminoAsString(), terminoNuevo);
        }
    }

    // ordena la Hashtable de terminos
    public void ordenarTerminos(){
        this.terminos = MapUtil.sortByKey(terminos);
    }

    // ordena la Hashtable de documentos
    public void ordenarDocumentos(){
        this.documentos = MapUtil.sortByKey(documentos);
    }

    // ordena las listas de posteos de cada termino
    public void ordenarPosteos(){

        for (Map.Entry<String, Termino> entry : terminos.entrySet()) {
            Termino termino = entry.getValue();
            termino.ordenarPosteos();
        }
    }

    // ordena una Hashtable y retorna una LinkedHashMap
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

    // muestra todos los terminos
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
}
