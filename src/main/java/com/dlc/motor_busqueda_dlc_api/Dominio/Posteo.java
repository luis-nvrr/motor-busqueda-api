package com.dlc.motor_busqueda_dlc_api.Dominio;

public class Posteo implements Comparable<Posteo>{
    private int frecuenciaTermino;
    private final Documento documento;

    public Posteo(Documento documento){
        this(documento, 1);
    }

    public Posteo(Documento documento, int frecuenciaTermino){
        this.documento = documento;
        this.frecuenciaTermino = frecuenciaTermino;
    }

    public void sumarFrecuencia(){
        this.frecuenciaTermino++;
    }

    public int getFrecuenciaTermino(){
        return frecuenciaTermino;
    }

    public String obtenerNombreDocumento(){
        return this.documento.getNombre();
    }

    public Documento getDocumento(){
        return this.documento;
    }

    public boolean tieneDocumento(Documento documento){
        return this.documento.equals(documento);
    }


    @Override
    public int compareTo(Posteo o) {
        if(o == null) { throw new NullPointerException(); }
        Posteo comparado = o;

        return this.frecuenciaTermino - comparado.frecuenciaTermino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o instanceof Posteo) return false;

        Posteo posteo = (Posteo) o;

        if (frecuenciaTermino != posteo.frecuenciaTermino) return false;
        return documento.equals(posteo.documento);
    }

    @Override
    public int hashCode() {
        int result = documento.hashCode();
        result = 31 * result + frecuenciaTermino;
        return result;
    }
}
