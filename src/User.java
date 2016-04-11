import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.Statement;


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
        List gameList = new ArrayList<Game>();
        Game currentGame = new Game();

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT Id, Name, Price FROM GAME JOIN OWNS ON Id = Game_id WHERE User_id = " + id );

            while (rs.next()) {         // read the result set
                currentGame.id = rs.getInt("id");
                currentGame.name = rs.getString("name");
                currentGame.price = rs.getFloat("price");

                gameList.add(currentGame);
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return gameList;
    }

    public List<User> friends() {
        List friendList = new ArrayList<User>();
        User currentUser = new User();

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT Real_name, Profile_name, Credit_card, Level, Phone, Id FROM FRIEND JOIN USER WHERE User1 = " + id + " OR User2 = " + id);

            while (rs.next()) {         // read the result set
                currentUser.realName = rs.getString("real_name");
                currentUser.profileName = rs.getString("profile_name");
                currentUser.creditCard = rs.getString("credit_card");
                currentUser.level = rs.getInt("level");
                currentUser.phone = rs.getString("phone");
                currentUser.id = rs.getInt("id");

                friendList.add(currentUser);
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return friendList;
    }

    public List<UserGroup> groups() {
        List groupList = new ArrayList<UserGroup>();
        UserGroup currentGroup = new UserGroup();

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT Name, User_Review.id FROM FRIEND JOIN USER WHERE User1 = " + id);

            while (rs.next()) {         // read the result set
                currentGroup.name = rs.getString("name");
                currentGroup.id = rs.getInt("id");

                groupList.add(currentGroup);
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return groupList;
    }

    public List<UserReview> reviews() {
        List reviewList = new ArrayList<UserReview>();
        UserReview currentReview = new UserReview();

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT Rating, Text, Game_id, User_id FROM USER_REVIEW JOIN USER ON User_id = id WHERE User_id = " + id);

            while (rs.next()) {         // read the result set
                currentReview.rating = rs.getFloat("rating");
                currentReview.text = rs.getString("text");
                currentReview.game.id = rs.getInt("game_id");
                currentReview.user.id = rs.getInt("user_id");

                reviewList.add(currentReview);
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return reviewList;
    }

    public List<User> all(){
        List userList = new ArrayList<User>();
        User currentUser = new User();

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT * FROM USER");

            while (rs.next()) {         // read the result set
                currentUser.realName = rs.getString("real_name");
                currentUser.profileName = rs.getString("profile_name");
                currentUser.creditCard = rs.getString("credit_card");
                currentUser.level = rs.getInt("level");
                currentUser.phone = rs.getString("phone");
                currentUser.id = rs.getInt("id");

                userList.add(currentUser);
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return userList;
    }
}
