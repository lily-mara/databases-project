import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 4/10/2016.
 */
public class Category extends Model {
    public int id;
    public String name;

    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<Category>();
        Connection c;
        try {
            c = DriverManager.getConnection("jdbc:sqlite:data.db");

            PreparedStatement s = c.prepareStatement(
                    "SELECT * FROM category"
            );
            ResultSet rs = s.executeQuery();
            Category category;
            while (rs.next()) {         // read the result set
                category = new Category();
                category.id = rs.getInt("id");
                category.name = rs.getString("name");

                categories.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public String toString() {
        return String.format("[Category %d: %s]", id, name);
    }
}
