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

        Game g = new Game();
        g.id = 1;
        g.name = "Brink";
        g.price = (float)19.99;

        System.out.println(g.categories());

        Object rowData[][] = {{"1", "2", "3"},
                {"4", "5", "6"}};
        Object columns[] = {"col1", "col2", "col3"};
        dbFrame.replaceTable(rowData, columns);
    }
}
