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

    public String getText() {
        return text;
    }
}
