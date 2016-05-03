import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nate on 4/10/16.
 */
public class UserGroup extends Model {
    public String name;
    public int id;

    public UserGroup(ResultSet rs) {
        try {
            name = rs.getString("Name");
            id = rs.getInt("Id");
        } catch (SQLException e) {
            try {
                name = rs.getString("User_group.Name");
                id = rs.getInt("User_group.Id");
            } catch (SQLException en) {
                en.printStackTrace();
            }
        }
    }

    public UserGroup(String name) {
        this.name = name;
        id = nextId();

        create();
    }

    public static int nextId() {
        try {
            PreparedStatement s = c.prepareStatement("SELECT MAX(id) FROM user_group");
            ResultSet rs = s.executeQuery();

            return rs.getInt(1) + 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean create() {
        try {
            PreparedStatement s = c.prepareStatement(
                    "INSERT INTO USER_GROUP (id, Name) VALUES " +
                            "(?,?)"
            );
            s.setInt(1, id);
            s.setString(2, name);

            return s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update() {
        try {
            PreparedStatement s = c.prepareStatement(
                    "UPDATE USER_GROUP SET Name=? where id=?"
            );

            s.setString(1, name);
            s.setInt(2, id);

            return s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasMember(User u) {
        return members().contains(u);
    }

    public boolean removeMember(User u) {
        try {
            PreparedStatement s = c.prepareStatement(
                    "DELETE FROM MEMBERSHIP WHERE Group_id=? AND User_id=?"
            );

            s.setInt(1, id);
            s.setInt(2, u.id);

            return s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addUser(User u) {
        try {
            PreparedStatement s = c.prepareStatement(
                    "INSERT INTO MEMBERSHIP (Group_id, User_id) VALUES (?,?)"
            );

            s.setInt(1, id);
            s.setInt(2, u.id);

            return s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> members() {
        List users = new ArrayList<>();

        try {
            PreparedStatement statement = c.prepareStatement("Select Id, Real_name, Profile_name, Credit_card, Level, phone, Password_hash" +
                    " from user join MEMBERSHIP on user.id = MEMBERSHIP.User_id" +
                    " where membership.Group_id=?");
            statement.setQueryTimeout(30);
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                users.add(new User(rs));
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return users;
    }

    public static List<UserGroup> all(){
        List userGroupList = new ArrayList<UserGroup>();

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT * FROM USER_GROUP");

            while (rs.next()) {
                userGroupList.add(new UserGroup(rs));
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return userGroupList;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserGroup) {
            return ((UserGroup)o).id == id;
        }
        return false;
    }
}

