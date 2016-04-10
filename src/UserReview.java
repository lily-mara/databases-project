/**
 * Created by Laura on 4/10/2016.
 */
public class UserReview implements Review {
    public double rating;
    public String text;
    public Game game;
    public User user;

    public String getText() {
        return text;
    }

    public Game getGame() {
        return game;
    }
}
