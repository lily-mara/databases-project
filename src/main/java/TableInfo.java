import java.util.List;

/**
 * Created by nate on 4/10/16.
 */
public class TableInfo {
    public Object rowData[][];
    public String columns[];

    public TableInfo(List<?> models) {
        if(models.isEmpty()){
            return;
        }
        if(models.get(0) instanceof User) {
            setupUser((List<User>)(List<?>) models);
        }
        if(models.get(0) instanceof Game) {
            setupGame((List<Game>)(List<?>) models);
        }
        if(models.get(0) instanceof Developer) {
            setupDeveloper((List<Developer>)(List<?>) models);
        }
        if(models.get(0) instanceof Category) {
            setupCategory((List<Category>)(List<?>) models);
        }
        if(models.get(0) instanceof CriticReview) {
            setupCriticReview((List<CriticReview>)(List<?>) models);
        }
        if(models.get(0) instanceof UserReview) {
            setupUserReview((List<UserReview>)(List<?>) models);
        }
        if(models.get(0) instanceof UserGroup) {
            setupUserGroup((List<UserGroup>)(List<?>) models);
        }
    }

    private void setupUserGroup(List<UserGroup> userGroups) {
        this.columns = new String[1];
        columns[0] = "Name";

        this.rowData = new Object[userGroups.size()][columns.length];
        for(int i = 0; i < userGroups.size(); i++){
            rowData[i][0] = userGroups.get(i).name;
        }
    }

    public void setupUserReview(List<UserReview> userReviews) {
        this.columns = new String[4];
        columns[0] = "Rating";
        columns[1] = "Text";
        columns[2] = "Game";
        columns[3] = "User";

        this.rowData = new Object[userReviews.size()][columns.length];
        for(int i = 0; i < userReviews.size(); i++){
            rowData[i][0] = userReviews.get(i).rating;
            rowData[i][1] = userReviews.get(i).text;
            rowData[i][2] = userReviews.get(i).game.name;
            rowData[i][3] = userReviews.get(i).user.profileName;
        }
    }

    public void setupCriticReview(List<CriticReview> criticReviews) {
        this.columns = new String[5];
        columns[0] = "Company";
        columns[1] = "Link";
        columns[2] = "Rating";
        columns[3] = "Text";
        columns[4] = "Game";

        this.rowData = new Object[criticReviews.size()][columns.length];
        for(int i = 0; i < criticReviews.size(); i++){
            rowData[i][0] = criticReviews.get(i).company;
            rowData[i][1] = criticReviews.get(i).link;
            rowData[i][2] = criticReviews.get(i).rating;
            rowData[i][3] = criticReviews.get(i).text;
            rowData[i][4] = criticReviews.get(i).game.name;
        }
    }

    public void setupCategory(List<Category> categories) {
        this.columns = new String[1];
        columns[0] = "Name";

        this.rowData = new Object[categories.size()][columns.length];
        for(int i = 0; i < categories.size(); i++){
            rowData[i][0] = categories.get(i).name;
        }
    }

    public void setupDeveloper(List<Developer> developers) {
        this.columns = new String[2];
        columns[0] = "Name";
        columns[1] = "Website";

        this.rowData = new Object[developers.size()][columns.length];
        for(int i = 0; i < developers.size(); i++){
            rowData[i][0] = developers.get(i).name;
            rowData[i][1] = developers.get(i).website;
        }

    }

    public void setupGame(List<Game> games) {
        this.columns = new String[2];
        columns[0] = "Name";
        columns[1] = "Price";

        this.rowData = new Object[games.size()][columns.length];
        for(int i = 0; i < games.size(); i++){
            rowData[i][0] = games.get(i).name;
            rowData[i][1] = games.get(i).price;
        }
    }

    public void setupUser(List<User> users) {
        this.columns = new String[3];
        columns[0] = "Real Name";
        columns[1] = "Profile Name";
        columns[2] = "Level";

        this.rowData = new Object[users.size()][columns.length];
        for(int i = 0; i < users.size(); i++){
            rowData[i][0] = users.get(i).realName;
            rowData[i][1] = users.get(i).profileName;
            rowData[i][2] = users.get(i).level;
        }
    }

}

