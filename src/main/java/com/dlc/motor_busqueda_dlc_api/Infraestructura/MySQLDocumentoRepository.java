package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import com.dlc.motor_busqueda_dlc_api.Dominio.DocumentoRepository;
import com.dlc.motor_busqueda_dlc_api.Dominio.Documento;

import java.sql.*;
import java.util.Hashtable;
import java.util.Map;

public class MySQLDocumentoRepository implements DocumentoRepository {

    private Connection connection;
    private Map<String, Documento> documentos;

    public Documento getDocumentoByName(String name){
        return documentos.get(name);
    }

    @Override
    public void saveDocumentos(Map<String, Documento> documentos) {
        try {
            connection = MySQLConnection.conectar();
            Statement statement = connection.createStatement();
            StringBuilder query =
                    new StringBuilder("INSERT INTO Documentos " +
                            "(nombre, path, link) VALUES ");

            for (Map.Entry<String, Documento> entry : documentos.entrySet()) {
                Documento documento = entry.getValue();
                String nombre = documento.getNombre();
                String path = (documento.getPath()).replace("\\", "\\\\");
                String link = documento.getLink();

                query.append("('").append(nombre).append("','")
                        .append(path).append("','")
                        .append(link).append("'),");
            }
            query.setCharAt(query.length()-1, ';');

            statement.execute(query.toString());
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Map<String, Documento> getAllDocumentos(){
        documentos = new Hashtable<>();
        try{
            connection = MySQLConnection.conectar();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Documentos";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                String nombre = resultSet.getString("nombre");
                String path = resultSet.getString("path");
                String link = resultSet.getString("link");

                Documento documento= new Documento(nombre, path); // TODO agregar link
                documentos.put(nombre, documento);
            }
            connection.close();
        }
        catch (SQLException exception){
            exception.printStackTrace();
        }

        return documentos;
    }
}
