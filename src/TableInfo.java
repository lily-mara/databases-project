import java.util.List;

/**
 * Created by nate on 4/10/16.
 */
public class TableInfo {
    public Object rowData[][];
    public String columns[];

    public TableInfo(List<User> users) {
        columns[0] = "Real Name";
        columns[1] = "Profile Name";
        columns[2] = "Credit Card";
        columns[3] = "Level";
        columns[4] = "Phone";
        columns[5] = "Id";

        for(int i = 0; i < users.size(); i++){
            rowData[i][0] = users.get(i).realName;
            rowData[i][1] = users.get(i).profileName;
            rowData[i][2] = users.get(i).creditCard;
            rowData[i][3] = users.get(i).level;
            rowData[i][4] = users.get(i).phone;
            rowData[i][5] = users.get(i).id;
        }
    }

    public TableInfo(List<Game> games) {
        columns[0] = "Name";
        columns[1] = "Id";
        columns[2] = "Price";

        for(int i = 0; i < games.size(); i++){
            rowData[i][0] = games.get(i).name;
            rowData[i][1] = games.get(i).id;
            rowData[i][2] = games.get(i).price;
        }
    }

    public TableInfo(List<Category> categories) {
        columns[0] = "Id";
        columns[1] = "Name";

        for(int i = 0; i < categories.size(); i++){
            rowData[i][0] = categories.get(i).id;
            rowData[i][1] = categories.get(i).name;
        }
    }

    public TableInfo(List<Developer> developers) {
        columns[0] = "Name";
        columns[1] = "Website";

        for(int i = 0; i < developers.size(); i++){
            rowData[i][0] = developers.get(i).name;
            rowData[i][1] = developers.get(i).website;
        }
    }

    public TableInfo(List<CriticReview> criticReviews) {
        columns[0] = "Company";
        columns[1] = "Link";
        columns[2] = "Rating";
        columns[3] = "Text";
        columns[4] = "Game Id";

        for(int i = 0; i < criticReviews.size(); i++){
            rowData[i][0] = criticReviews.get(i).company;
            rowData[i][1] = criticReviews.get(i).link;
            rowData[i][2] = criticReviews.get(i).rating;
            rowData[i][3] = criticReviews.get(i).text;
            rowData[i][4] = criticReviews.get(i).game.id;
        }
    }

    public TableInfo(List<UserReview> userReviews) {
        columns[0] = "Rating";
        columns[1] = "Text";
        columns[2] = "Game Id";
        columns[3] = "User Id";

        for(int i = 0; i < userReviews.size(); i++){
            rowData[i][0] = userReviews.get(i).rating;
            rowData[i][1] = userReviews.get(i).text;
            rowData[i][2] = userReviews.get(i).game.id;
            rowData[i][3] = userReviews.get(i).user.id;
        }
    }

    public TableInfo(List<UserGroup> userGroups) {
        columns[0] = "Name";
        columns[1] = "Id";

        for(int i = 0; i < userGroups.size(); i++){
            rowData[i][0] = userGroups.get(i).name;
            rowData[i][1] = userGroups.get(i).id;
        }
    }


}

