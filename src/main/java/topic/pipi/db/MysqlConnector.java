package topic.pipi.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MysqlConnector {

    private static String HOST;
    private static Integer PORT;
    private static String DATABASE;
    private static String USERNAME;
    private static String PASSWORD;

    public static String TABLE = "time_series_data";

    private static ComboPooledDataSource dataSource = new ComboPooledDataSource();

    public static void init() {
        try {
            loadProperties();
            createDataSource();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void createDataSource() throws PropertyVetoException {
        dataSource.setJdbcUrl(
                String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8",
                        HOST,
                        PORT,
                        DATABASE));
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");// or com.mysql.jdbc.Driver (Deprecated)
        dataSource.setAcquireIncrement(5);
        dataSource.setInitialPoolSize(5);
        dataSource.setMinPoolSize(1);
        dataSource.setMaxPoolSize(100);
    }

    private static void loadProperties() throws IOException {
        Properties properties = new Properties();

        properties.load(new FileReader("app.properties"));
        HOST = properties.getProperty("db.mysql.host");
        PORT = Integer.valueOf(properties.getProperty("db.mysql.port"));
        DATABASE = properties.getProperty("db.mysql.database");
        USERNAME = properties.getProperty("db.mysql.username");
        PASSWORD = properties.getProperty("db.mysql.password");
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void returnConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
