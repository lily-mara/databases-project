/**
 * Created by Laura on 4/10/2016.
 */
public class CriticReview implements Review{
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
}
