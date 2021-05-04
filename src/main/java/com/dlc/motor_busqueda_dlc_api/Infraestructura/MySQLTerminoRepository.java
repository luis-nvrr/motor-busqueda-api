package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import com.dlc.motor_busqueda_dlc_api.Dominio.Termino;
import com.dlc.motor_busqueda_dlc_api.Dominio.TerminoRepository;

import java.sql.*;
import java.util.Hashtable;
import java.util.Map;

public class MySQLTerminoRepository implements TerminoRepository {

    private Connection connection;

    @Override
    public Termino getTermino(String termino) {
        return null;
    }

    @Override
    public void saveTermino(Termino termino) {
    }

    @Override
    public Map<String, Termino> getAllTerminos() {
        Map<String, Termino> terminos = new Hashtable<>();
        try{
            connection = MySQLConnection.conectar();
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
            exception.printStackTrace();
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
                String palabra = entry.getValue().getTermino();
                int cantidadDocumentos = entry.getValue().getCantidadDocumentos();
                int maximaFrecuenciaTermino = entry.getValue().getMaximaFrecuenciaTermino();

                query.append("('").append(palabra).append("',")
                        .append(cantidadDocumentos).append(",")
                        .append(maximaFrecuenciaTermino).append("),");
            }
            query.setCharAt(query.length()-1, ' ');
            query.append("ON DUPLICATE KEY UPDATE cantidadDocumentos = cantidadDocumentos+1, ")
                    .append("maximaFrecuenciaTermino = IF(maximaFrecuenciaTermino > VALUES(maximaFrecuenciaTermino),")
                    .append("maximaFrecuenciaTermino, VALUES(maximaFrecuenciaTermino))");

            statement.execute(query.toString());
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
