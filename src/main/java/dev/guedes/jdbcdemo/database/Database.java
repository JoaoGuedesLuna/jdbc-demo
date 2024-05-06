package dev.guedes.jdbcdemo.database;

import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.exception.PropertiesException;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author João Guedes
 */
@UtilityClass
public class Database {

    private static final Properties properties;

    static {
        String propertiesPath = System.getProperty("user.dir").concat("\\src\\main\\resources\\application.properties");
        try {
            @Cleanup FileInputStream fileInputStream = new FileInputStream(propertiesPath);
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new PropertiesException(e.getMessage());
        }
    }

    public static Connection getConnection() throws DatabaseException {
        try {
            final String URL = properties.getProperty("jdbc.datasource.url");
            final String USERNAME = properties.getProperty("jdbc.datasource.username");
            final String PASSWORD = properties.getProperty("jdbc.datasource.password");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

}