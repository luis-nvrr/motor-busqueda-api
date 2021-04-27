package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DocumentoRecuperado implements Comparable<DocumentoRecuperado> {
    private String path;
    private String nombre;
    private int indiceRelevancia;

    public DocumentoRecuperado(String path, String nombre){
        this.path = path;
        this.nombre = nombre;
        this.indiceRelevancia = 0;
    }

    public void sumarRelevancia(int relevancia){
        this.indiceRelevancia += relevancia;
    }

    @Override
    public int compareTo(DocumentoRecuperado o) {
        return o.indiceRelevancia - this.indiceRelevancia;
    }

    public String getPath() {
        return path;
    }

    public String getNombre() {
        return nombre;
    }

    public int getIndiceRelevancia() {
        return indiceRelevancia;
    }

    public String getTexto(){
        try {
            return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "";
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("path: ").append(path).append("\n")
                .append("nombre: ").append(nombre).append("\n")
                .append("indice de relevancia: ").append(indiceRelevancia).append("\n");

        return stringBuilder.toString();
    }
}
