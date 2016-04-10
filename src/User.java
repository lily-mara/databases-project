import java.util.List;

/**
 * Created by nate on 4/10/16.
 */
public class User extends Model {
    public String realName;
    public String profileName;
    public String creditCard;
    public int level;
    public String phone;
    public int id;

    public List<Game> games() {
        return null;
    }

    public List<User> friends() {
        return null;
    }

    public List<UserGroup> groups() {
        return null;
    }

    public List<UserReview> reviews() {
        return null;
    }
}
