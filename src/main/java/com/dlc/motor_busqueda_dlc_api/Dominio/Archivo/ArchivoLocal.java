package com.dlc.motor_busqueda_dlc_api.Dominio.Archivo;

import java.io.*;

public class ArchivoLocal implements IArchivo {
    private final String path;
    private BufferedReader bufferedReader;
    private final String nombre;
    private final File archivo;


    public ArchivoLocal(String path){
        this.path = path;
        this.archivo = new File(path);
        this.nombre = archivo.getName();
    }

    public void openReader(){
        try{
            bufferedReader = new BufferedReader(new FileReader(path));
        }
        catch(FileNotFoundException exception){
            exception.printStackTrace();
        }
    }

    public String obtenerSiguienteLinea(){
        String linea = "";
        try{
            linea = bufferedReader.readLine();
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
        return linea;
    }

    public void closeReader(){
        try{
            bufferedReader.close();
        }
        catch(IOException exception){
            exception.printStackTrace();
        }
    }

    public String obtenerPath() {
        return path;
    }

    public String obtenerNombre(){ return nombre; }
}
