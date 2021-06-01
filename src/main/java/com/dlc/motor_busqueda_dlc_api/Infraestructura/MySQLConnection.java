package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

public class MySQLConnection {
    public static final String USER = System.getenv("USERNAME");
    public  static final String PASSWORD = System.getenv("PASSWORD");
    public static String dbURL = "jdbc:mysql://localhost:3306/" + System.getenv("DATABASE")
            + "?verifyServerCertificate=false"
            + "&useSSL=false"
            + "&requireSSL=false"
            + "&sslMode=disabled";

    public Connection conectar(){
        try {

            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("jdbc/MySQLPool");
            Connection conn = ds.getConnection();
            return conn;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Connection conectarJDBC(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.
                    getConnection(MySQLConnection.dbURL,
                            MySQLConnection.USER, MySQLConnection.PASSWORD);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
