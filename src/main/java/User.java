import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import org.mindrot.BCrypt;


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
    private String passwordHash;

    public List<Game> games() {
        List<Game> games = new ArrayList<Game>();

        try {
            PreparedStatement s = c.prepareStatement(
                    "SELECT Game.id, name, price FROM GAME JOIN OWNS ON Game.Id = Game_id JOIN USER ON User.Id = User_id WHERE USER.Id=?"
            );
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();
            Game g;
            while (rs.next()) {         // read the result set
                g = new Game();
                g.id = rs.getInt("id");
                g.name = rs.getString("name");
                g.price = rs.getFloat("price");//This probably isn't needed

                games.add(g);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public static User getUserByProfileName(String profileName) {
        try {
            PreparedStatement s = c.prepareStatement(
                    "select * from user where profile_name=? COLLATE NOCASE"
            );
            s.setString(1, profileName);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.id = rs.getInt("id");
                u.realName = rs.getString("real_name");
                u.profileName = rs.getString("profile_name");
                u.creditCard = rs.getString("credit_card");
                u.level = rs.getInt("level");
                u.phone = rs.getString("phone");
                u.passwordHash = rs.getString("password_hash");

                return u;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean owns(Game g) {
        return games().contains(g);
    }

    public UserReview writeReview(Game g, double rating, String text) {
        if (owns(g)) {
            UserReview u = new UserReview();
            u.rating = rating;
            u.user = this;
            u.game = g;
            u.text = text;

            u.create();

            return u;
        } else {
            return null;
        }
    }

    public List<User> friends() {
        List friendList = new ArrayList<User>();
        User currentUser;

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            PreparedStatement s = c.prepareStatement("SELECT DISTINCT Real_name, Profile_name, Credit_card, Level, Phone, Id, Password_hash FROM FRIEND JOIN USER ON User1 = Id WHERE User2=?" +
                    " UNION SELECT DISTINCT Real_name, Profile_name, Credit_card, Level, Phone, Id, Password_hash FROM FRIEND JOIN USER ON User2 = Id WHERE User1=?");
            s.setInt(1, id);
            s.setInt(2, id);
            ResultSet rs = s.executeQuery();

            while (rs.next()) {         // read the result set
                currentUser = new User();
                currentUser.realName = rs.getString("real_name");
                currentUser.profileName = rs.getString("profile_name");
                currentUser.creditCard = rs.getString("credit_card"); //Shouldn't be here
                currentUser.level = rs.getInt("level");
                currentUser.phone = rs.getString("phone");
                currentUser.id = rs.getInt("id");
                currentUser.passwordHash = rs.getString("password_hash");

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

    public static List<User> all(){
        List userList = new ArrayList<User>();
        try {
            PreparedStatement s = c.prepareStatement("SELECT * FROM USER");
            ResultSet rs = s.executeQuery();

            while (rs.next()) {         // read the result set
                User currentUser = new User();
                currentUser.realName = rs.getString("real_name");
                currentUser.profileName = rs.getString("profile_name");
                currentUser.creditCard = rs.getString("credit_card");
                currentUser.level = rs.getInt("level");
                currentUser.phone = rs.getString("phone");
                currentUser.id = rs.getInt("id");
                currentUser.passwordHash = rs.getString("password_hash");

                userList.add(currentUser);
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return userList;
    }

    public ErrorCode purchaseGame( int GameId ){
        //Need to check for credit card/ownership
        if(creditCard == null) {
            return new ErrorCode(ErrorResult.FAIL, "No credit card");
        }

        try {
            PreparedStatement s = c.prepareStatement("INSERT INTO OWNS VALUES(?,?)");
            s.setInt(1, GameId);
            s.setInt(2, id);
            s.execute();
            return new ErrorCode(ErrorResult.SUCCESS, "Congratulations, you bought a game!");

        }catch(SQLException e){
            System.err.println(e);
        }

        return new ErrorCode(ErrorResult.FAIL, "You already own the game");
    }

    public boolean addFriend(int friendId){
        try {
            PreparedStatement s = c.prepareStatement("INSERT INTO FRIEND VALUES(?,?)");
            s.setInt(1, id);
            s.setInt(2, friendId);
            s.execute();
            return true;
        }catch(SQLException e){
            System.err.println(e);
        }
        return false;
    }

    public boolean removeFriend(int friendId){
        try {
            PreparedStatement s = c.prepareStatement("DELETE FROM FRIEND WHERE (User1=? AND User2=?) OR (User1=? AND User2=?)");
            s.setInt(1, id);
            s.setInt(2, friendId);
            s.setInt(3, friendId);
            s.setInt(4, id);
            s.execute();
            return true;
        }catch(SQLException e){
            System.err.println(e);
        }
        return false;
    }

    private void setPasswordUnsafe(String password) {
        passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());

        try {
            PreparedStatement s = c.prepareStatement("UPDATE GAME SET Password_hash=? WHERE Id=?");
            s.setString(1, passwordHash);
            s.setInt(2, id);
        } catch(SQLException e){
            System.err.println(e);
        }
    }

    public void setPassword(String currentPassword, String newPassword) {
        if (isPasswordValid(currentPassword)) {
            setPasswordUnsafe(newPassword);
        }
    }

    public boolean isPasswordValid(String password) {
        return BCrypt.checkpw(password, passwordHash);
    }

    @Override
    public boolean equals(Object o){
        User u;
        if(o instanceof User){
            u = (User)o;
            return u.id == this.id;
        }
        return false;
    }
}
