package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import com.dlc.motor_busqueda_dlc_api.Dominio.DocumentoRepository;
import com.dlc.motor_busqueda_dlc_api.Dominio.Documento;

import java.sql.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

public class MySQLDocumentoRepository implements DocumentoRepository {

    private MySQLConnection database = new MySQLConnection();
    private Connection connection;
    private static Logger logger = Logger.getLogger("global");

    @Override
    public void saveDocumentos(Map<String, Documento> documentos) {
        try {
            connection = database.conectar();

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
            query.setCharAt(query.length()-1, ';');

            statement.execute(query.toString());
            connection.close();
            statement.close();

        } catch (SQLException exception) {
            logger.info(exception.getMessage());
        }
    }

    @Override
    public void bulkSaveDocumentos(Map<String, Documento> documentos) {
        try {
            connection = database.conectarJDBC();

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

            BulkInsertHelper.bulkInsert("documentos", stringBuilder.toString(), statement);
            connection.close();
            statement.close();

        } catch (SQLException exception) {
            logger.info(exception.getMessage());
        }
    }

    public Map<String, Documento> getAllDocumentos(){
        Map<String, Documento> documentos = new Hashtable<>();
        try{
            connection = database.conectar();
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
            statement.close();
            resultSet.close();
            return documentos;
        }
        catch (SQLException exception){
            logger.info(exception.getMessage());
        }
        return null;
    }

    @Override
    public Documento getDocumento(String documentoString) {
        try{
            connection = database.conectar();
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM Documentos WHERE nombre LIKE '%s'", documentoString);
            ResultSet resultSet = statement.executeQuery(query);

            resultSet.next();
            String nombre = resultSet.getString("nombre");
            String path = resultSet.getString("path");
            Documento documento= new Documento(nombre, path);

            connection.close();
            statement.close();
            resultSet.close();
            return documento;
        }
        catch (SQLException exception){
            logger.info(exception.getMessage());
        }
        return null;
    }
}
