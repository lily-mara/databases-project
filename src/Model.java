import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by nate on 4/10/16.
 */
public abstract class Model {
    protected static Connection c = connect();

    public Model() {
    }

    private static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
