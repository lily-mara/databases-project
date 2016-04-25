import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Laura on 4/10/2016.
 */
public class UserReview extends Model implements Review {
    public double rating;
    public String text;
    public Game game;
    public User user;

    public Game getGame() {
        return game;
    }

    public double getRating() {
        return rating;
    }

    public void create() {
        try {
            PreparedStatement s = c.prepareStatement(
                    "insert into USER_REVIEW (Game_id, Rating, User_id, Text) values (?,?,?,?)"
            );
            s.setInt(1, game.id);
            s.setDouble(2, rating);
            s.setInt(3, user.id);
            s.setString(4, text);

            s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getText() {
        return text;
    }
}
