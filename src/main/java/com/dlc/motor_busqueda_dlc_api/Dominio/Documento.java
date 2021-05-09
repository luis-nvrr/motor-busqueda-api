package com.dlc.motor_busqueda_dlc_api.Dominio;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Documento {
    private final String nombre;
    private final String path;


    public Documento(String nombre, String path){
        this.nombre = nombre;
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Documento documento = (Documento) o;

        if (!nombre.equals(documento.nombre)) return false;
        return path.equals(documento.path);
    }

    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }

    public String getTexto(){
        try {
            return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}

