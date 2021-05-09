package com.dlc.motor_busqueda_dlc_api.Infraestructura;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BulkInsertHelper {

    public static String bulkInsert(String tabla, String values){
        String path = String.format("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\%s.csv", tabla);
        File file = new File(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(values);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        StringBuilder query;
        query = new StringBuilder();
        query.append("SET UNIQUE_CHECKS=0;")
                .append("SET FOREIGN_KEY_CHECKS=0;")
                .append("TRUNCATE ").append(tabla).append(";")
                .append("SET AUTOCOMMIT=0;");
        query.append("LOAD DATA INFILE ")
                .append("'").append("C:\\\\ProgramData\\\\MySQL\\\\MySQL Server 8.0\\\\Uploads\\\\")
                .append(tabla).append(".csv").append("'")
                .append(" INTO TABLE ").append(tabla)
                .append(" FIELDS TERMINATED BY ','")
                .append(" ENCLOSED BY '\"';");
        query.append("COMMIT;");

        return query.toString();
    }
}
