import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.event.*;


/**
 * Created by loomisdf on 4/10/2016.
 */

public class DatabaseFrame{

    private JFrame frame;
    private JLabel descriptionLabel, userGreeting;
    private JPanel panel;
    private JPanel loginScreen;
    private JButton goButton;
    private JButton purchaseButton;
    private JTable gameTable;
    private DefaultTableModel gameTableModel;
    private JComboBox dropdown;
    private JTabbedPane userScreen;
    private JButton friends;
    private JButton ownedGames;
    private JTable friendUserTable;
    private JTable gameUserTable;
    private DefaultTableModel friendUserTableModel;
    private DefaultTableModel gameUserTableModel;
    private JPanel friendListPanel;
    private JPanel friendPagePanel;
    private JLabel friendNameLabel;
    private JScrollPane friendTableScrollPanel;
    private JButton backToFriends;
    private JScrollPane gameTableScrollPanel;

    private JTextArea addReview;
    private JPanel gameListPanel;
    private JPanel gamePagePanel;
    private JPanel reviewsPanel;
    private JButton gameReviewSubmit;
    private JComboBox<Integer> gameRating;

    private JLabel gameNameLabel;
    private JScrollPane gameTableScrollPane;

    private JPanel addFriendPanel;
    private JTextField addFriendText;
    private JButton addFriendButton;
    private JButton removeFriend;
    private JButton backToStore;
    private JButton newUserButton;

    // New user creation panel
    private JPanel newUserPanel;
    private JTextField realNameInput;
    private JTextField newUserInput;
    private JPasswordField newUserPassInput;
    private JPasswordField newUserPassInput2;
    private JButton newUserSubmit;
    private JLabel passwordMismatchLabel;

    //Profile Things
    private JButton update;
    private JButton restore;
    private JTextField RealName;
    private JTextField ProfileName;
    private JTextField CreditCard;
    private JTextField Phone;
    private JPanel profilePanel;

    private JButton searchSubmit;
    private Game currentGame;

    private User currentUser;
    private JPanel topPanel;

    private JPanel card2;
    private JTabbedPane userTabbedPane;

    // USER Group panel
    private JTable userGroupTable;
    private DefaultTableModel userGroupTableModel;
    private JButton backToUserGroups;
    private JButton showUserGroups;


    public DatabaseFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame = new JFrame("Database Frame");
        panel = new JPanel();
        descriptionLabel = new JLabel("Welcome to the DB");
        searchSubmit = new JButton("Search");

        searchSubmit.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Search for game by title.");
            TableInfo t = new TableInfo(Game.search(input));
            if (t.columns != null) {
                updateTable(gameTable, gameTableModel, t.rowData, t.columns);
                purchaseButton.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "No game matching that search :(");
            }
        });

        // Various creation operations
        createDropdown();
        createButtons();
        createTable();
        createLoginScreen();
        createProfileScreen();
        createUserScreen();
        createNewUserPage();


        // Setup JFrame
        frame.setSize(500, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);

        // Add things to panel
        panel.add(userScreen);
        panel.add(loginScreen);
        panel.add(newUserPanel);

        // Add panel to frame
        frame.add(panel);

        // Set the frame visible
        frame.setVisible(true);
    }

    private void createNewUserPage() {
        newUserPanel = new JPanel();
        newUserPanel.setLayout(new BoxLayout(newUserPanel, BoxLayout.PAGE_AXIS));

        JLabel realNameLabel = new JLabel("Enter your real name");
        JLabel userInputLabel = new JLabel("Choose a user name");
        JLabel passInputLabel = new JLabel("Choose a password");
        JLabel passInputLabel2 = new JLabel("Re-enter password");

        newUserInput = new JTextField(null, 15);
        realNameInput = new JTextField(null, 15);

        newUserPassInput = new JPasswordField(null, 15);
        newUserPassInput2 = new JPasswordField(null, 15);

        newUserSubmit = new JButton("Submit");

        passwordMismatchLabel = new JLabel("Passwords don't match!");
        passwordMismatchLabel.setForeground(Color.RED);
        passwordMismatchLabel.setVisible(false);

        newUserSubmit.addActionListener(e -> {
            String pass1 = String.valueOf(newUserPassInput.getPassword());
            String pass2 = String.valueOf(newUserPassInput2.getPassword());
            if((pass1 == "" || pass2 == "")) {
                passwordMismatchLabel.setVisible(true);
            }
            else if(pass1.equals(pass2)) {
                //passwords match
                new User(realNameInput.getText(), newUserInput.getText(), String.valueOf(newUserPassInput.getPassword()));

                //Go back to login
                loginScreen.setVisible(true);
                newUserPanel.setVisible(false);
            }
            else {
                passwordMismatchLabel.setVisible(true);
            }

        });
        newUserPanel.add(realNameLabel);
        newUserPanel.add(realNameInput);
        newUserPanel.add(userInputLabel);
        newUserPanel.add(newUserInput);
        newUserPanel.add(passInputLabel);
        newUserPanel.add(newUserPassInput);
        newUserPanel.add(passInputLabel2);
        newUserPanel.add(newUserPassInput2);
        newUserPanel.add(passwordMismatchLabel);
        newUserPanel.add(newUserSubmit);

        newUserPanel.setVisible(false);
    }

    private void createLoginScreen() {
        loginScreen = new JPanel();


        // Create Labels
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        final JLabel userNotFoundWarning = new JLabel("User not found!");
        userNotFoundWarning.setForeground(Color.red);
        userNotFoundWarning.setVisible(false);

        final JLabel invalidPasswordWarning = new JLabel("Invalid password!");
        invalidPasswordWarning.setForeground(Color.red);
        invalidPasswordWarning.setVisible(false);

        // Create Inputs
        final JTextField userNameInput = new JTextField(15);
        final JPasswordField passInput = new JPasswordField(15);

        // Button Functionality
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener((ActionEvent e) -> {
            currentUser = User.getUserByProfileName(userNameInput.getText());
            if(currentUser != null) {
                if (currentUser.isPasswordValid(String.valueOf(passInput.getPassword()))) {
                    loginScreen.setVisible(false);
                    userGreeting.setText("Hello " + currentUser.realName);
                    userScreen.setVisible(true);
                    restore.doClick();
                } else {
                    invalidPasswordWarning.setVisible(true);
                }
            }
            else {
                userNotFoundWarning.setVisible(true);
            }
        });

        try {
            JLabel logo = new JLabel(new ImageIcon("src" + File.separator + "MistLogo2.png"));
            loginScreen.add(logo);
        }catch(Exception E){
            E.printStackTrace();
        }

        KeyListener enterListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    currentUser = User.getUserByProfileName(userNameInput.getText());
                    if(currentUser != null) {
                        if (currentUser.isPasswordValid(String.valueOf(passInput.getPassword()))) {
                            loginScreen.setVisible(false);
                            userGreeting.setText("Hello " + currentUser.realName);
                            userScreen.setVisible(true);
                            restore.doClick();
                        } else {
                            invalidPasswordWarning.setVisible(true);
                        }
                    }
                    else {
                        userNotFoundWarning.setVisible(true);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };

        userNameInput.addKeyListener(enterListener);
        passInput.addKeyListener(enterListener);

        // New user button
        newUserButton = new JButton("New User");
        newUserButton.addActionListener(e -> {
            loginScreen.setVisible(false);
            newUserPanel.setVisible(true);
        });

        loginScreen.add(userLabel);
        loginScreen.add(userNameInput);

        loginScreen.add(passLabel);
        loginScreen.add(passInput);

        loginScreen.add(loginButton);
        loginScreen.add(newUserButton);
        loginScreen.add(userNotFoundWarning);
        loginScreen.add(invalidPasswordWarning);
        loginScreen.setLayout(new BoxLayout(loginScreen, BoxLayout.PAGE_AXIS));
    }

    private void createUserScreen() {
        userScreen = new JTabbedPane();

        // Store Panel
        JPanel card1 = new JPanel();

        gameListPanel = new JPanel();
        gameListPanel.add(descriptionLabel);
        gameListPanel.add(dropdown);
        gameListPanel.add(goButton);
        //gameListPanel.add(purchaseButton);
        gameListPanel.add(searchSubmit);
        gameListPanel.setLayout(new FlowLayout());


        //Create the gamePanel
        createGamePagePanel();
        gamePagePanel.setVisible(false);

        card1.add(gameListPanel);
        card1.add(gamePagePanel);
        gameTableScrollPanel = new JScrollPane(gameTable);
        card1.add(gameTableScrollPanel);

        card1.setLayout(new BoxLayout(card1, BoxLayout.Y_AXIS));

        // User Page
        createFriendPagePanel();
        friendPagePanel.setVisible(false);

        card2 = new JPanel();
        card2.add(friendPagePanel);
        userGreeting = new JLabel("Hello Unknown User");


        addFriendText = new JTextField("New Friend", 10);
        addFriendPanel = new JPanel();
        addFriendPanel.add(addFriendText);
        addFriendPanel.add(addFriendButton);
        addFriendPanel.add(removeFriend);

        topPanel = new JPanel();
        topPanel.add(userGreeting);

        card2.add(topPanel);


        JScrollPane friendTableScrollPanel = new JScrollPane(friendUserTable);
        userTabbedPane.addTab("My Friends", friendTableScrollPanel);
        JScrollPane gameTableScrollPanel = new JScrollPane(gameUserTable);
        userTabbedPane.addTab("My Games", gameTableScrollPanel);
        JScrollPane userGroupScrollPanel = new JScrollPane(userGroupTable);
        userTabbedPane.addTab("My Groups", userGroupScrollPanel);




        card2.add(userTabbedPane);

        //card2.setComponentZOrder(friendTableScrollPanel, 2);
        //card2.setComponentZOrder(gameTableScrollPanel, 2);

        card2.add(addFriendPanel);
        card2.setLayout(new BoxLayout(card2, BoxLayout.Y_AXIS));

        // Create the friend panel

        //createFriendPagePanel();
        //friendPagePanel.setVisible(false);

        //card2.add(friendListPanel);
        //card2.add(friendPagePanel);
        //friendTableScrollPane = new JScrollPane(friendTable);
        //card2.add(friendTableScrollPane);

        //card2.setLayout(new BoxLayout(card2, BoxLayout.Y_AXIS));

        //Profile settings
        JPanel card3 = new JPanel();
        profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        card3.setLayout(new BoxLayout(card3, BoxLayout.Y_AXIS));
        card3.add(profilePanel);

        JPanel formPanel = new JPanel();

        SpringLayout layout = new SpringLayout();
        formPanel.setLayout(layout);
        JLabel realNameLabel = new JLabel("Real Name:", JLabel.TRAILING);
        formPanel.add(realNameLabel);
        formPanel.add(RealName);
        JLabel profileNameLabel = new JLabel("Profile Name:", JLabel.TRAILING);
        formPanel.add(profileNameLabel);
        formPanel.add(ProfileName);
        JLabel creditCardLabel = new JLabel("Credit Card:", JLabel.TRAILING);
        formPanel.add(creditCardLabel);
        formPanel.add(CreditCard);
        JLabel phoneLabel = new JLabel("Phone:", JLabel.TRAILING);
        formPanel.add(phoneLabel);
        formPanel.add(Phone);

        SpringUtilities.makeCompactGrid(formPanel, 4, 2, 10, 10, 5, 55);
        profilePanel.add(formPanel);
        profilePanel.add(update);
        profilePanel.add(restore);

        userScreen.addTab("Store", card1);
        userScreen.addTab("User Page", card2);
        userScreen.addTab("Profile Settings", card3);
        userScreen.setVisible(false);
    }

    private void createDropdown() {
        String[] options = {"Show All Games", "Show All Categories"};
        dropdown = new JComboBox(options);
    }

    private void createTable() {
        gameTable = new JTable();
        friendUserTable = new JTable();
        gameUserTable = new JTable();
        userGroupTable = new JTable();

        friendUserTableModel = new DefaultTableModel(0, 0);
        gameUserTableModel = new DefaultTableModel(0,0);
        gameTableModel = new DefaultTableModel(0, 0);
        userGroupTableModel = new DefaultTableModel(0, 0);

        // Double click on a user's friend's name
        friendUserTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table =(JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                String rowItem = (String) table.getValueAt(row, 0);
                if (me.getClickCount() == 2) {
                    friendUserTable.setVisible(false);
                    updateFriendPagePanel(rowItem);
                    friendPagePanel.setVisible(true);
                }
            }
        });

        // Double click on user's game title
        gameUserTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table =(JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                int gameId = Integer.parseInt(table.getValueAt(row, 1).toString());
                currentGame = Game.getGameById(gameId);
                if (me.getClickCount() == 2) {
                    userScreen.setSelectedIndex(0);
                    updateGamePagePanel(currentGame);
                }
            }
        });

        // Double click on Group
        userGroupTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table =(JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                String rowItem = (String) table.getValueAt(row, 0);
                if (me.getClickCount() == 2) {
                    friendUserTable.setVisible(false);

                    updateFriendPagePanel(rowItem);
                    friendPagePanel.setVisible(true);
                }
            }
        });

        // Double click on a game title
        gameTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table =(JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                int gameId = Integer.parseInt(table.getValueAt(row, 1).toString());
                currentGame = Game.getGameById(gameId);
                if (me.getClickCount() == 2) {
                    // your valueChanged overridden method

                    updateGamePagePanel(currentGame);
                }
            }
        });
    }

    public void updateTable(JTable cTable, DefaultTableModel model, Object rowData[][], Object columns[]){
        if(rowData == null || columns == null)
            return;

        model.setColumnIdentifiers(columns);
        cTable.setModel(model);

        // Remove Rows
        if (model.getRowCount() > 0) {
            for (int i = model.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }

        // Add new rows
        for(int r = 0; r < rowData.length; r++) {
            model.addRow(rowData[r]);
        }

    }

    private void createFriendPagePanel() {
        friendPagePanel = new JPanel();
        friendNameLabel = new JLabel("Friend Default Name");
        friendPagePanel.add(friendNameLabel);

        // Add a back button to return to the friend's list
        backToFriends = new JButton("Back");
        backToFriends.addActionListener(e -> {
            userScreen.setSelectedIndex(1);
            userTabbedPane.setVisible(true);
            topPanel.setVisible(true);
            addFriendPanel.setVisible(true);
            friendPagePanel.setVisible(false);
        });
        friendPagePanel.add(backToFriends);
    }

    //Navigated to, through store panel
    private void createGamePagePanel() {
        gamePagePanel = new JPanel();
        gamePagePanel.setLayout(new BoxLayout(gamePagePanel, BoxLayout.Y_AXIS));
        gameNameLabel = new JLabel("This shouldn't be showing yet");
        gamePagePanel.add(gameNameLabel);
        gamePagePanel.add(purchaseButton);

        // Add a back button to return to the store
        backToStore = new JButton("Back");
        backToStore.addActionListener(e -> {
            userScreen.setSelectedIndex(0);
            gameListPanel.setVisible(true);
           // gameTable.setVisible(true);
            gameTableScrollPanel.setVisible(true);
            gamePagePanel.setVisible(false);

        });
        gamePagePanel.add(backToStore);


        reviewsPanel = new JPanel();

        addReview = new JTextArea("Add a review", 3, 30);
        addReview.setPreferredSize(new Dimension(3, 30));
        addReview.setLineWrap(true);
        addReview.setEditable(true);

        gameRating = new JComboBox<Integer>();
        gameRating.addItem(1);
        gameRating.addItem(2);
        gameRating.addItem(3);
        gameRating.addItem(4);
        gameRating.addItem(5);
        gameRating.addItem(6);
        gameRating.addItem(7);
        gameRating.addItem(8);
        gameRating.addItem(9);
        gameRating.addItem(10);

        reviewsPanel.add(addReview);
        reviewsPanel.add(gameRating);
        reviewsPanel.add(gameReviewSubmit);

        gamePagePanel.add(reviewsPanel);
    }

    private void updateFriendPagePanel(String friendName) {
        friendNameLabel.setText(friendName);
        addFriendPanel.setVisible(false);
        userTabbedPane.setVisible(false);
        topPanel.setVisible(false);
        friendPagePanel.setVisible(true);
    }

    private void updateGamePagePanel(Game game) {
        gameListPanel.setVisible(false);
        gameTableScrollPanel.setVisible(false);
        gameTable.setVisible(false);
        gameNameLabel.setText(game.name);
        gamePagePanel.setVisible(true);
    }

    private void createButtons() {
        goButton = new JButton("Go!");
        goButton.addActionListener((ActionEvent e) -> {
            String operation = (String) dropdown.getSelectedItem();
            switch(operation) {
                case("Show All Games"): {
                    TableInfo t = new TableInfo(Game.getAllGames());
                    updateTable(gameTable, gameTableModel,t.rowData, t.columns);
                    purchaseButton.setVisible(true);
                    break;
                }
                case("Show All Categories") : {
                    TableInfo t = new TableInfo(Category.getAllCategories());
                    updateTable(gameTable, gameTableModel, t.rowData, t.columns);
                    purchaseButton.setVisible(false);
                    break;
                }
            }
        });


        purchaseButton = new JButton("Purchase");
        purchaseButton.setVisible(false);
        purchaseButton.addActionListener(e -> {
            ErrorCode errorCode = currentUser.purchaseGame(currentGame.id);
            if(errorCode.result == ErrorResult.FAIL) {
                JOptionPane.showMessageDialog(frame, errorCode.message);
            }
            else if(errorCode.result == ErrorResult.SUCCESS) {
                JOptionPane.showMessageDialog(frame, errorCode.message);
            }
        });

        userTabbedPane = new JTabbedPane();
        userTabbedPane.addChangeListener(new ChangeListener() {
             @Override
             public void stateChanged(ChangeEvent e) {
                 TableInfo t;
                 switch(currentUser == null ? -1 : userTabbedPane.getSelectedIndex()) {
                     case 0:
                         addFriendPanel.setVisible(true);
                         t = new TableInfo(currentUser.friends());
                         if (t.columns != null) {
                             updateTable(friendUserTable, friendUserTableModel, t.rowData, t.columns);
                         } else {
                             JOptionPane.showMessageDialog(frame,
                                     "You have no friends :(");
                         }
                         break;
                     case 1:
                         addFriendPanel.setVisible(false);
                         t = new TableInfo(currentUser.games());
                         if (t.columns != null) {
                             updateTable(gameUserTable, gameUserTableModel, t.rowData, t.columns);
                         } else {
                             JOptionPane.showMessageDialog(frame,
                                     "You have no games :(");
                         }
                         break;
                     case 2:
                         addFriendPanel.setVisible(false);
                         t = new TableInfo(currentUser.groups());
                         if (t.columns != null) {
                             updateTable(userGroupTable, userGroupTableModel, t.rowData, t.columns);
                         } else {
                             JOptionPane.showMessageDialog(frame,
                                     "You have no groups :(");
                         }
                         break;
                     default:
                         break;

                 }
             }
         });

        addFriendButton = new JButton("Add");
        addFriendButton.addActionListener((ActionEvent e)->{
            User friend = User.getUserByProfileName(addFriendText.getText());

            //if the username exists and you're not already friends
            if(User.all().contains(friend) && !currentUser.friends().contains(friend)) {
                currentUser.addFriend(friend.id);
            }
            friends.doClick();
        });

        removeFriend = new JButton("Remove Selected Friend");
        removeFriend.addActionListener((ActionEvent e)->{
            int row = friendUserTable.getSelectedRow();
            String friendName = (String) friendUserTable.getValueAt(row,1);
            int friendId = User.getUserByProfileName(friendName).id;
            currentUser.removeFriend(friendId);
            friends.doClick();
        });

        gameReviewSubmit = new JButton("Submit");
        gameReviewSubmit.addActionListener((ActionEvent e)->{
            UserReview r = currentUser.writeReview(currentGame, (int)gameRating.getSelectedItem(), addReview.getText());
            if(r == null) {
                JOptionPane.showMessageDialog(frame, String.format("Sorry %s, I'm afraid I can't let you do that.", currentUser.realName));
            }
            updateGamePagePanel(currentGame);
        });
    }

    private void createProfileScreen(){
        update = new JButton("Update Information");
        restore = new JButton("Restore");
        RealName = new JTextField();
        ProfileName = new JTextField();
        CreditCard = new JTextField();
        Phone = new JTextField();

        update.addActionListener((ActionEvent e)->{
            currentUser.realName = RealName.getText();
            currentUser.profileName = ProfileName.getText();
            try {
                currentUser.setCreditCard(CreditCard.getText());
                currentUser.setPhone(Phone.getText());
            }catch(IllegalArgumentException error){
                JOptionPane.showMessageDialog(panel,error.getMessage());
            }

            currentUser.update();
        });

        restore.addActionListener((ActionEvent e)->{
            RealName.setText(currentUser.realName);
            ProfileName.setText(currentUser.profileName);
            CreditCard.setText(currentUser.getCreditCard());
            Phone.setText(currentUser.getPhone());
        });


    }
}
