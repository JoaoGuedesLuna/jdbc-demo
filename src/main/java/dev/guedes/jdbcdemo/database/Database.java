package dev.guedes.jdbcdemo.database;

import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.exception.PropertiesException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author João Guedes
 */
public class Database {

    public static Connection getConnection() throws DatabaseException {
        try {
            Properties properties = Database.loadProperties();
            final String URL = properties.getProperty("jdbc.datasource.url");
            final String USERNAME = properties.getProperty("jdbc.datasource.username");
            final String PASSWORD = properties.getProperty("jdbc.datasource.password");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch (PropertiesException e) {
            throw new RuntimeException(e.getMessage());
        }
        catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private static Properties loadProperties() throws PropertiesException {
        String propertiesPath = System.getProperty("user.dir").concat("\\src\\main\\resources\\application.properties");
        try {
            FileInputStream fileInputStream = new FileInputStream(propertiesPath);
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException e) {
            throw new PropertiesException(e.getMessage());
        }
    }

}