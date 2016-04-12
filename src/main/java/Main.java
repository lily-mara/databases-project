import java.util.List;

/**
 * Created by loomisdf on 4/10/2016.
 */

public class Main {

    static DatabaseFrame dbFrame;

    public static void main(String[] args) {
        dbFrame = new DatabaseFrame();
        // load the sqlite-JDBC driver using the current class loader
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
