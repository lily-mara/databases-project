import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nate on 4/10/16.
 */
public class UserGroup extends Model {
    public String name;
    public int id;

    public List<User> members() {
        return null;
    }

    public List<UserGroup> all(){
        List userGroupList = new ArrayList<UserGroup>();
        UserGroup currentUserGroup = new UserGroup();

        try {
            Statement statement = c.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            ResultSet rs = statement.executeQuery("SELECT * FROM GAME");

            while (rs.next()) {         // read the result set
                currentUserGroup.name = rs.getString("Name");
                currentUserGroup.id = rs.getInt("Id");

                userGroupList.add(currentUserGroup);
            }
        }catch(SQLException e){
            System.err.println(e);
        }

        return userGroupList;
    }
}

