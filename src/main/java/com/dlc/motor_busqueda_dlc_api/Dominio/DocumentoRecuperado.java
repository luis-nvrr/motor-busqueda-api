package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DocumentoRecuperado implements Comparable<DocumentoRecuperado> {
    private String nombre;
    private double indiceRelevancia;

    public DocumentoRecuperado(String nombre){
        this.nombre = nombre;
        this.indiceRelevancia = 0;
    }

    public void sumarRelevancia(double relevancia){
        this.indiceRelevancia += relevancia;
    }

    @Override
    public int compareTo(DocumentoRecuperado o) {
        return (int)o.indiceRelevancia - (int)this.indiceRelevancia;
    }

    public String getNombre() {
        return nombre;
    }

    public double getIndiceRelevancia() {
        return indiceRelevancia;
    }



    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("nombre: ").append(nombre).append("\n")
                .append("indice de relevancia: ").append(indiceRelevancia).append("\n");

        return stringBuilder.toString();
    }
}
