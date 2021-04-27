package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import java.sql.*;

public class MySQLConnection {
    public static final String USER = "root";
    public  static final String PASSWORD = "";
    public static String dbURL = "jdbc:mysql://localhost:3306/motor-busqueda-dlc"
            + "?verifyServerCertificate=false"
            + "&useSSL=false"
            + "&requireSSL=false"
            + "&sslMode=disabled";

    public static Connection conectar(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.
                    getConnection(MySQLConnection.dbURL,
                            MySQLConnection.USER, MySQLConnection.PASSWORD);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
