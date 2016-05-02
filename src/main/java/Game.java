import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Game extends Model {
    public int id;
    public String name;
    public float price;

    public Game() {

    }

    private Game(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        name = rs.getString("name");
        price = rs.getFloat("price");
    }

    public static List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        try {
            PreparedStatement s = c.prepareStatement(
                    "SELECT id, name, price from game"
            );
            ResultSet rs = s.executeQuery();

            while (rs.next()) {
                 games.add(new Game(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public static Game getGameById(int id) {
        try {
            PreparedStatement s = c.prepareStatement(
                    "SELECT id, name, price from game where id=?"
            );
            s.setInt(1, id);

            ResultSet rs = s.executeQuery();
            return new Game(rs);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Game> search(String key) {
        List<Game> games = new ArrayList<>();

        key = "%" + key + "%";

        try {
            PreparedStatement s = c.prepareStatement(
                    "SELECT id, name, price from game where name like ?"
            );
            s.setString(1, key);

            ResultSet rs = s.executeQuery();
            while (rs.next()) {         // read the result set
                games.add(new Game(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public List<Category> categories() {
        try {
            List<Category> categories = new ArrayList<>();
            Category cat;

            PreparedStatement s = c.prepareStatement(
                    "SELECT id, name from game_category join category on id=category_id where game_id=?"
            );
            s.setInt(1, id);

            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                cat = new Category();
                cat.id = rs.getInt("id");
                cat.name = rs.getString("name");

                categories.add(cat);
            }

            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public float getCriticRating() {
        try {
            PreparedStatement s = c.prepareStatement("SELECT AVG(rating) from CRITIC_REVIEW where game_id=? GROUP BY GAME_ID");
            s.setInt(1, id);

            ResultSet rs = s.executeQuery();
            return rs.getFloat(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public float getUserRating() {
        try {
            PreparedStatement s = c.prepareStatement("SELECT AVG(rating) from USER_REVIEW where game_id=? GROUP BY GAME_ID");
            s.setInt(1, id);

            ResultSet rs = s.executeQuery();
            return rs.getFloat(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Review> reviews() {
        try {
            List<Review> reviews = new ArrayList<>();
            UserReview ur;
            CriticReview cr;
            User u;

            PreparedStatement s = c.prepareStatement(
                    "SELECT rating, text, real_name, profile_name, credit_card, level, phone, user.id" +
                            "from user_review" +
                            "join user on user.id=user_review.user_id" +
                            "where game_id=?"
            );
            s.setInt(1, id);

            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                ur = new UserReview();
                ur.rating = rs.getInt("id");
                ur.text = rs.getString("text");
                ur.game = this;

                u = new User(rs);
                ur.user = u;
                reviews.add(ur);
            }

            s = c.prepareStatement("SELECT rating, text, company, link from critic_review where game_id=?");
            s.setInt(1, id);

            rs = s.executeQuery();
            while (rs.next()) {
                cr = new CriticReview();

                cr.game = this;
                cr.company = rs.getString("company");
                cr.rating = rs.getFloat("rating");
                cr.link = rs.getString("link");

                reviews.add(cr);
            }

            return reviews;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("Game <name=%s>", name);
    }
}
