package dal.db;

/**
 *
 * @author fawad
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MySQLConnection implements IConnection {
    private static final Logger LOGGER = Logger.getLogger(MySQLConnection.class.getName());

    private final String dbName;
    private final String username;
    private final String password;

    public MySQLConnection(String dbName, String username, String password) {
        this.dbName = dbName;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, username, password);
        } catch (SQLException ex) {
            // Log at FINE so normal test runs don't show the low-level driver message by default.
            LOGGER.log(Level.FINE, "Connection problem while obtaining MySQL connection", ex);
        }
        return null;
    }

    // Example of closing the connection
    public void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.FINE, "Error while closing connection", ex);
        }
    }
}


