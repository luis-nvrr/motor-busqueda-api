package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.util.*;

import static java.util.Map.*;

public class Termino implements Comparable<Termino>{

    private List<Posteo> posteos;
    private final String termino;
    private int cantidadDocumentos;
    private int maximaFrecuenciaTermino;


    public Termino(String termino){
        this(termino, 0, 1);
    }

    public Termino(String termino, int cantidadDocumentos, int maximaFrecuenciaTermino){
        this.posteos = new ArrayList<>();
        this.termino = termino;
        this.cantidadDocumentos = cantidadDocumentos;
        this.maximaFrecuenciaTermino = maximaFrecuenciaTermino;
    }

    public List<Posteo> getPosteos(){
        return posteos;
    }

    public void setPosteos(List<Posteo> posteos){
        this.posteos = posteos;
    }

    public String getTerminoAsString() {
        return termino;
    }

    public int getCantidadDocumentos() {
        return cantidadDocumentos;
    }

    public int getMaximaFrecuenciaTermino() {
        return maximaFrecuenciaTermino;
    }

    public void agregarPosteo(Documento documento){
        Posteo recuperado = buscarPosteo(documento);

        if(recuperado == null){ agregarNuevoPosteo(documento);}
        else{
            actualizarPosteoExistente(recuperado);
        }
    }

    private void agregarNuevoPosteo(Documento documento){
        Posteo posteo = new Posteo(documento);
        cantidadDocumentos++;
        agregarAListaPosteos(posteo);
    }

    private Posteo buscarPosteo(Documento documento){

        for (Posteo posteo : posteos) {
            if (posteo.tieneDocumento(documento)) {
                return posteo;
            }
        }
        return null;
    }

    private void actualizarPosteoExistente(Posteo posteo){
        posteo.sumarFrecuencia();
        actualizarFrecuenciaMaxima(posteo);
    }

    private void actualizarFrecuenciaMaxima(Posteo posteo){
        if (posteo.getFrecuenciaTermino() > maximaFrecuenciaTermino){
            maximaFrecuenciaTermino = posteo.getFrecuenciaTermino();
        }
    }

    public void agregarAListaPosteos(Posteo posteo){
        this.posteos.add(posteo);
    }

    public String mostrarOrdenPosteo(){
        Iterator<Posteo> it = posteos.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("termino: ").append(termino).append("\n\t");

        while(it.hasNext()) {
            stringBuilder.append("frecuencia: ")
                    .append(it.next().getFrecuenciaTermino())
                    .append(" ");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    @Override
    public int compareTo(Termino o) {
        return this.cantidadDocumentos - o.cantidadDocumentos;
    }


    public void ordenarPosteos(){
        posteos.sort(Comparator.comparing(Posteo::obtenerNombreDocumento));
    }
}
