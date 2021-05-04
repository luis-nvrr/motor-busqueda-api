package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import com.dlc.motor_busqueda_dlc_api.Dominio.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MySQLPosteoRepository implements PosteoRepository {
    private Connection connection;

    @Override
    public Posteo getPosteo() {
        return null;
    }

    @Override
    public List<Posteo> getAllPosteosByTermino(String termino, Map<String, Documento> documentos) throws TerminoNoEncontradoException {
        List<Posteo> posteosRecuperados = new ArrayList<>();

        try{
            connection = MySQLConnection.conectar();
            Statement statement = connection.createStatement();
            String query = String.format("SELECT * FROM Posteos WHERE termino LIKE '%s' ORDER BY frecuenciaTermino DESC", termino);
            ResultSet resultSet = statement.executeQuery(query);

            if (!resultSet.isBeforeFirst()){
                throw new TerminoNoEncontradoException();
            }

            while(resultSet.next()){
                String documentoString = resultSet.getString("nombre"); // TODO cambiar nombre por documento
                int frecuenciaTermino = resultSet.getInt("frecuenciaTermino");

                Documento documento = documentos.get(documentoString);
                Posteo posteo = new Posteo(documento, frecuenciaTermino);
                posteosRecuperados.add(posteo);
            }
            connection.close();
            return posteosRecuperados;
        }
        catch (SQLException exception){
            exception.printStackTrace();
        }
        return posteosRecuperados;
    }

    @Override
    public void savePosteo(Posteo posteo) {
    }

    @Override
    public void savePosteos(Map<String, Termino> terminos) {
        try {
            connection = MySQLConnection.conectar();
            Statement statement = connection.createStatement();
            StringBuilder query =
                    new StringBuilder("INSERT INTO Posteos " +
                            "(nombre, termino, frecuenciaTermino) VALUES ");


            for (Map.Entry<String, Termino> entry : terminos.entrySet()) {
                String palabra = entry.getKey();
                Termino termino = entry.getValue();
                List<Posteo> listaPosteos = termino.getPosteos();

                for (Posteo posteo : listaPosteos) {
                    String documento = posteo.obtenerNombreDocumento();
                    int frecuenciaTermino = posteo.getFrecuenciaTermino();

                    query.append("('").append(documento).append("','")
                            .append(palabra).append("',")
                            .append(frecuenciaTermino).append("),");
                }
            }

            query.setCharAt(query.length()-1, ';');

            statement.execute(query.toString());
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
