package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class BulkInsertHelper {

    public static void bulkInsert(String tabla, String values, Statement statement){
        String path = String.format("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\%s.csv", tabla);
        File file = new File(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(values);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try {
            statement.addBatch("SET UNIQUE_CHECKS=0");
            statement.addBatch("SET FOREIGN_KEY_CHECKS=0");
            statement.addBatch(String.format("TRUNCATE %s",tabla));
            statement.addBatch("SET AUTOCOMMIT=0");
            statement.addBatch(String.format("LOAD DATA INFILE " +
                    "'C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\%s.csv' " +
                    "INTO TABLE %s " +
                    "FIELDS TERMINATED BY ',' " +
                    "ENCLOSED BY '\"'", tabla, tabla));
            statement.addBatch("COMMIT");
            statement.executeBatch();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
