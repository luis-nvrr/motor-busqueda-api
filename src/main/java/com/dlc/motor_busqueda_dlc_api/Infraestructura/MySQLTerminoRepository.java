package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import com.dlc.motor_busqueda_dlc_api.Dominio.Termino;
import com.dlc.motor_busqueda_dlc_api.Dominio.TerminoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MySQLTerminoRepository implements TerminoRepository {

    private Connection connection;
    private static Logger logger = Logger.getLogger("global");

    @Override
    public Map<String, Termino> getAllTerminos() {
        Map<String, Termino> terminos = new Hashtable<>();
        try{
            connection = MySQLConnection.conectar();

            assert connection != null;
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM Terminos";
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                String palabra = resultSet.getString("termino");
                int cantidadDocumentos = resultSet.getInt("cantidadDocumentos");
                int maximaFrecuenciaTermino = resultSet.getInt("maximaFrecuenciaTermino");

                Termino termino = new Termino(palabra, cantidadDocumentos, maximaFrecuenciaTermino);
                terminos.put(palabra, termino);
            }
            connection.close();
        }
        catch (SQLException exception){
            logger.info(exception.getMessage());
        }

        return terminos;
    }

    @Override
    public List<Termino> getTerminos(String[] terminosString) {
        List<Termino> terminos = new ArrayList<>();
        try{
            connection = MySQLConnection.conectar();

            assert connection != null;
            Statement statement = connection.createStatement();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT * FROM terminos where termino IN (");

            for (String terminoString : terminosString){
                stringBuilder.append("'").append(terminoString).append("',");
            }
            stringBuilder.setCharAt(stringBuilder.length()-1, ')');
            stringBuilder.append(";");

            String query = stringBuilder.toString();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                String palabra = resultSet.getString("termino");
                int cantidadDocumentos = resultSet.getInt("cantidadDocumentos");
                int maximaFrecuenciaTermino = resultSet.getInt("maximaFrecuenciaTermino");

                Termino termino = new Termino(palabra, cantidadDocumentos, maximaFrecuenciaTermino);
                terminos.add(termino);
            }
            connection.close();
        }
        catch (SQLException exception){
            logger.info(exception.getMessage());
        }

        return terminos;

    }

    @Override
    public void saveTerminos(Map<String, Termino> terminos) {
        try {
            connection = MySQLConnection.conectar();
            Statement statement = connection.createStatement();
            StringBuilder query =
                    new StringBuilder("INSERT INTO Terminos " +
                            "(termino, cantidadDocumentos, maximaFrecuenciaTermino) VALUES ");

            for (Map.Entry<String, Termino> entry : terminos.entrySet()) {
                String palabra = entry.getValue().getTerminoAsString();
                int cantidadDocumentos = entry.getValue().getCantidadDocumentos();
                int maximaFrecuenciaTermino = entry.getValue().getMaximaFrecuenciaTermino();

                query.append("('").append(palabra).append("',")
                        .append(cantidadDocumentos).append(",")
                        .append(maximaFrecuenciaTermino).append("),");
            }
            query.deleteCharAt(query.length()-1);
            query.append(" ON DUPLICATE KEY UPDATE cantidadDocumentos = cantidadDocumentos + 1, ")
                    .append("maximaFrecuenciaTermino = IF(maximaFrecuenciaTermino > VALUES(maximaFrecuenciaTermino),")
                    .append("maximaFrecuenciaTermino, VALUES(maximaFrecuenciaTermino))");

            statement.execute(query.toString());
            connection.close();

        } catch (SQLException exception) {
            logger.info(exception.getMessage());
        }
    }

    @Override
    public void bulkSaveTerminos(Map<String, Termino> terminos) {

        try {
            connection = MySQLConnection.conectar();
            Statement statement = connection.createStatement();
            StringBuilder stringBuilder = new StringBuilder();

            for (Map.Entry<String, Termino> entry : terminos.entrySet()) {
                String palabra = entry.getValue().getTerminoAsString();
                int cantidadDocumentos = entry.getValue().getCantidadDocumentos();
                int maximaFrecuenciaTermino = entry.getValue().getMaximaFrecuenciaTermino();

                stringBuilder.append("\"").append(palabra).append("\",\"")
                        .append(cantidadDocumentos).append("\",\"")
                        .append(maximaFrecuenciaTermino).append("\",")
                        .append("\n");
            }

            String query = BulkInsertHelper.bulkInsert("terminos", stringBuilder.toString());
            statement.executeQuery(query);
            connection.close();

        } catch (SQLException exception) {
            logger.info(exception.getMessage());
        }


    }
}
