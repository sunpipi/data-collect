package topic.pipi.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnector {

    private static String HOST;
    private static Integer PORT;
    private static String DATABASE;
    private static String USERNAME;
    private static String PASSWORD;

    public static String TABLE;


    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    public static void init() {
        try {
            ds.setJdbcUrl(
                    String.format("jdbc:mysql://%s:%d/%s?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8",
                            HOST,
                            PORT,
                            DATABASE));
            ds.setUser(USERNAME);
            ds.setPassword(PASSWORD);
            ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setAcquireIncrement(5);
            ds.setInitialPoolSize(2);
            ds.setMinPoolSize(2);
            ds.setMaxPoolSize(20);
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return ds.getConnection();
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
