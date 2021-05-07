package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import com.dlc.motor_busqueda_dlc_api.Dominio.DocumentoRepository;
import com.dlc.motor_busqueda_dlc_api.Dominio.Documento;

import java.io.*;
import java.sql.*;
import java.util.Hashtable;
import java.util.Map;

public class MySQLDocumentoRepository implements DocumentoRepository {

    private Connection connection;

    @Override
    public void saveDocumentos(Map<String, Documento> documentos) {
        try {
            connection = MySQLConnection.conectar();

            assert connection != null;
            Statement statement = connection.createStatement();

            StringBuilder query =
                    new StringBuilder("INSERT INTO Documentos " +
                            "(nombre, path) VALUES ");

            for (Map.Entry<String, Documento> entry : documentos.entrySet()) {
                Documento documento = entry.getValue();
                String nombre = documento.getNombre();
                String path = (documento.getPath()).replace("\\", "\\\\");

                query.append("('").append(nombre).append("','")
                        .append(path).append("'),");
            }
            query.setCharAt(query.length()-1, ' ');
            query.append("ON DUPLICATE KEY UPDATE nombre=nombre");

            statement.execute(query.toString());
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void bulkSaveDocumentos(Map<String, Documento> documentos) {
        try {
            connection = MySQLConnection.conectar();

            assert connection != null;
            Statement statement = connection.createStatement();
            StringBuilder stringBuilder = new StringBuilder();

            for (Map.Entry<String, Documento> entry : documentos.entrySet()) {
                Documento documento = entry.getValue();
                String nombre = documento.getNombre();
                String path = (documento.getPath()).replace("\\", "\\\\");

                stringBuilder.append("\"").append(nombre).append("\",\"")
                        .append(path).append("\",")
                        .append("\n");
            }

            String query = BulkInsertHelper.bulkInsert("documentos", stringBuilder.toString());
            statement.executeQuery(query);
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public Map<String, Documento> getAllDocumentos(){
        Map<String, Documento> documentos = new Hashtable<>();
        try{
            connection = MySQLConnection.conectar();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Documentos";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                String nombre = resultSet.getString("nombre");
                String path = resultSet.getString("path");

                Documento documento= new Documento(nombre, path);
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
