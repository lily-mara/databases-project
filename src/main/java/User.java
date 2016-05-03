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
    private String creditCard;
    public int level;
    private String phone;
    public int id;
    private String passwordHash;

    public User() {
        level = 1;
        id = nextId();
    }

    public User(String realName, String profileName, String password) {
        this.realName = realName;
        this.profileName = profileName;
        level = 1;
        id = nextId();

        setPasswordUnsafe(password);

        save();
    }

    public User(ResultSet rs) {
        try {
            profileName = rs.getString("user.profile_name");
            realName = rs.getString("user.real_name");
            creditCard = rs.getString("user.credit_card");
            level = rs.getInt("user.level");
            phone = rs.getString("user.phone");
            id = rs.getInt("user.id");
            passwordHash = rs.getString("user.password_hash");
        } catch (SQLException e) {
            try {
                profileName = rs.getString("profile_name");
                realName = rs.getString("real_name");
                creditCard = rs.getString("credit_card");
                level = rs.getInt("level");
                phone = rs.getString("phone");
                id = rs.getInt("id");
                passwordHash = rs.getString("password_hash");
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public boolean save() {
        try {
            PreparedStatement s = c.prepareStatement(
                    "INSERT INTO USER (id, Real_name, Profile_name, Credit_card, Level, phone, Password_hash) VALUES " +
                            "(?,?,?,?,?,?,?)"
            );
            s.setInt(1, id);
            s.setString(2, realName);
            s.setString(3, profileName);
            s.setString(4, creditCard);
            s.setInt(5, level);
            s.setString(6, phone);
            s.setString(7, passwordHash);

            return s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update() {
        try {
            PreparedStatement s = c.prepareStatement(
                    "UPDATE USER SET Real_name=?, Profile_name=?, Credit_card=?, Level=?, phone=?, Password_hash=? where id=?"
            );

            s.setString(1, realName);
            s.setString(2, profileName);
            s.setString(3, creditCard);
            s.setInt(4, level);
            s.setString(5, phone);
            s.setString(6, passwordHash);
            s.setInt(7, id);

            return s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setCreditCard(String cc) {
        cc = cc.replaceAll("[^0-9]", "");

        if (cc.length() != 16 && !cc.equals("")) {
            throw new IllegalArgumentException("Credit card numbers must be 16 numbers!");
        }

        if(cc.equals("")){
            creditCard = null;
        }
        else
            creditCard = cc;
    }

    public void setPhone(String p) {
        p = p.replaceAll("[^0-9]", "");

        if (p.length() != 10 && !p.equals("")) {
            throw new IllegalArgumentException("Phone numbers must be 10 numbers!");
        }

        if (p.equals(""))
            phone = null;
        else
            phone = p;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public static int nextId() {
        try {
            PreparedStatement s = c.prepareStatement("SELECT MAX(id) FROM user");
            ResultSet rs = s.executeQuery();

            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

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
                User u = new User(rs);
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

    public UserReview writeReview(Game g, int rating, String text) {
        if (owns(g)) {
            UserReview u = new UserReview();
            u.rating = rating;
            u.user = this;
            u.game = g;
            u.text = text;

            if(!u.create()) {
                return null;
            }

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
                currentUser = new User(rs);
                friendList.add(currentUser);
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return friendList;
    }

    public List<UserGroup> groups() {
        List groupList = new ArrayList<UserGroup>();

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT Name, User_Review.id FROM FRIEND JOIN USER WHERE User1 = " + id);

            while (rs.next()) {         // read the result set
                groupList.add(new UserGroup(rs));
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
                User currentUser = new User(rs);
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
        update();
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
