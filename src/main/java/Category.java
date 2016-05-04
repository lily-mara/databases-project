import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 4/10/2016.
 */
public class Category extends Model {
    public int id;
    public String name;

    public Category(ResultSet rs) {
        try {
            id = rs.getInt("id");
            name = rs.getString("name");
        } catch (SQLException e) {
            try {
                id = rs.getInt("category.id");
                name = rs.getString("category.name");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            PreparedStatement s = c.prepareStatement(
                    "SELECT * FROM category"
            );
            ResultSet rs = s.executeQuery();
            while (rs.next()) {         // read the result set
                categories.add(new Category(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static Category getCategoryByName(String name) {
        try {
            PreparedStatement s = c.prepareStatement(
                    "SELECT * FROM category where name=?"
            );
            s.setString(1, name);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                return new Category(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Game> games() {
        List<Game> games = new ArrayList<>();
        try {
            PreparedStatement s = c.prepareStatement(
                    "SELECT Game.Id, Game.Name, Game.Price FROM Game" +
                            " JOIN GAME_CATEGORY ON GAME.Id = GAME_CATEGORY.Game_id" +
                            " where GAME_CATEGORY.Category_id=?"
            );
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                games.add(new Game(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    @Override
    public String toString() {
        return String.format("[Category %d: %s]", id, name);
    }
}
