/**
 * Created by Laura on 4/10/2016.
 */
public class CriticReview extends Model implements Review {
    public String company;
    public String link;
    public double rating;
    public String text;
    public Game game;

    public Game getGame() {
        return game;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return String.format("[CriticReview %s's review of %s]", company, game);
    }
}
