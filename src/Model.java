import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by nate on 4/10/16.
 */
public abstract class Model {
    protected Connection c;

    public Model() {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:data.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Model(Connection c) {
        this.c = c;
    }
}
