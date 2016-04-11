import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nate on 4/10/16.
 */
public class Game extends Model {
    public int id;
    public String name;
    public float price;

    public List<Category> categories() {
        try {
            List<Category> categories = new ArrayList<Category>();
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

                u = new User();
                ur.user = u;
                u.realName = rs.getString("real_name");
                u.creditCard = rs.getString("credit_card");
                u.level = rs.getInt("level");
                u.phone = rs.getString("phone");
                u.id = rs.getInt("user.id");

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

        public List<Game> all(){
        List gameList = new ArrayList<Game>();
        Game currentGame = new Game();

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT * FROM GAME");

            while (rs.next()) {         // read the result set
                currentGame.name = rs.getString("Name");
                currentGame.id = rs.getInt("Id");
                currentGame.price = rs.getFloat("Price");

                gameList.add(currentGame);
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return gameList;
    }
}
