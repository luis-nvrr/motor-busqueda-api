package com.dlc.motor_busqueda_dlc_api.Dominio;

public class Documento {
    private final String nombre;
    private final String path;
    private String link;


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

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Documento documento = (Documento) o;

        if (!nombre.equals(documento.nombre)) return false;
        if (!path.equals(documento.path)) return false;
        return link.equals(documento.link);
    }

    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + link.hashCode();
        return result;
    }
}

