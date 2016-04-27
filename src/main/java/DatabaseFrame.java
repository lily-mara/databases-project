import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.event.*;
import java.util.ArrayList;

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
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JPanel gameListPanel;
    private JPanel gamePagePanel;
    private JLabel gameNameLabel;
    private JScrollPane gameTableScrollPane;
    private JTextField addFriendText;
    private JButton addFriendButton;
    private JButton removeFriend;
    private JButton backToStore;
    private JButton searchSubmit;
    private JTextField searchField;

    private User currentUser;

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
        searchField = new JTextField();

        searchSubmit.addActionListener(e -> {
            TableInfo t = new TableInfo(Game.search(searchField.getText()));
            if (t.columns != null) {
                replaceTable(gameTable, gameTableModel, t.rowData, t.columns);
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
        createUserScreen();
        createLoginScreen();

        // Setup JFrame
        frame.setSize(500, 620);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);

        // Add things to panel
        panel.add(userScreen);
        panel.add(loginScreen);

        // Add panel to frame
        frame.add(panel);

        // Set the frame visible
        frame.setVisible(true);
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
                    userGreeting.setText("Hello " + currentUser.profileName);
                    userScreen.setVisible(true);
                } else {
                    invalidPasswordWarning.setVisible(true);
                }
            }
            else {
                userNotFoundWarning.setVisible(true);
            }
        });

        try {
            loginScreen.add(new JLabel(new ImageIcon("src" + File.separator + "MistLogo2.png")));
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
                            userGreeting.setText("Hello " + currentUser.profileName);
                            userScreen.setVisible(true);
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

        loginScreen.add(userLabel);
        loginScreen.add(userNameInput);

        loginScreen.add(passLabel);
        loginScreen.add(passInput);

        loginScreen.add(loginButton);
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
        gameListPanel.add(purchaseButton);
        gameListPanel.add(searchField);
        gameListPanel.add(searchSubmit);
        gameListPanel.setLayout(new FlowLayout());


        //Create the gamePanel
        createGamePagePanel();
        gamePagePanel.setVisible(false);

        card1.add(gameListPanel);
        card1.add(gamePagePanel);
        gameTableScrollPane = new JScrollPane(gameTable);
        card1.add(gameTableScrollPane);

        card1.setLayout(new BoxLayout(card1, BoxLayout.Y_AXIS));

        // User Page
        JPanel card2 = new JPanel();
        userGreeting = new JLabel("Hello Unknown User");


        addFriendText = new JTextField("New Friend", 10);
        JPanel addFriendPanel = new JPanel();
        addFriendPanel.add(addFriendText);
        addFriendPanel.add(addFriendButton);
        addFriendPanel.add(removeFriend);


        card2.add(userGreeting);
        card2.add(friends);
        card2.add(ownedGames);

        card2.add(new JScrollPane(userTable));
        card2.add(addFriendPanel);
        card2.setLayout(new BoxLayout(card2, BoxLayout.Y_AXIS));

        userScreen.addTab("Store", card1);
        userScreen.addTab("User Page", card2);

        userScreen.setVisible(false);
    }

    private void createDropdown() {
        String[] options = {"Show All Games", "Show All Categories"};
        dropdown = new JComboBox(options);
    }

    private void createTable() {
        gameTable = new JTable();
        userTable = new JTable();
        gameTableModel = new DefaultTableModel(0, 0);
        userTableModel = new DefaultTableModel(0, 0);

        // Double click on a game title
        gameTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                JTable table =(JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                String gameTitle = (String) table.getValueAt(row, 0);
                if (me.getClickCount() == 2) {
                    // your valueChanged overridden method
                    gameListPanel.setVisible(false);
                    updateGamePagePanel(gameTitle);
                    gamePagePanel.setVisible(true);
                    gameTableScrollPane.setVisible(false);
                }
            }
        });
    }

    public void replaceTable(JTable cTable, DefaultTableModel model, Object rowData[][], Object columns[]) {
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

    private void createGamePagePanel() {
        gamePagePanel = new JPanel();
        gameNameLabel = new JLabel("This shouldn't be showing yet");
        gamePagePanel.add(gameNameLabel);

        // Add a back button to return to the store
        backToStore = new JButton("Back");
        backToStore.addActionListener(e -> {
            gameListPanel.setVisible(true);
            gamePagePanel.setVisible(false);
            gameTableScrollPane.setVisible(true);
        });
        gamePagePanel.add(backToStore);
    }

    private void updateGamePagePanel(String gameName) {
        gamePagePanel.setVisible(true);
        gameNameLabel.setText(gameName);
    }

    private void createButtons() {
        goButton = new JButton("Go!");
        goButton.addActionListener((ActionEvent e) -> {
            String operation = (String) dropdown.getSelectedItem();
            switch(operation) {
                case("Show All Games"): {
                    TableInfo t = new TableInfo(Game.getAllGames());
                    replaceTable(gameTable, gameTableModel,t.rowData, t.columns);
                    purchaseButton.setVisible(true);
                    break;
                }
                case("Show All Categories") : {
                    TableInfo t = new TableInfo(Category.getAllCategories());
                    replaceTable(gameTable, gameTableModel, t.rowData, t.columns);
                    purchaseButton.setVisible(false);
                    break;
                }
            }
        });


        purchaseButton = new JButton("Purchase");
        purchaseButton.setVisible(false);
        purchaseButton.addActionListener(e -> {
            int row = gameTable.getSelectedRow();
            int GameId = (Integer) gameTable.getValueAt(row,1);

            ErrorCode errorCode = currentUser.purchaseGame(GameId);
            if(errorCode.result == ErrorResult.FAIL) {
                JOptionPane.showMessageDialog(frame, errorCode.message);
            }
            else if(errorCode.result == ErrorResult.SUCCESS) {
                JOptionPane.showMessageDialog(frame, errorCode.message);
            }

            if (row == -1) {
                return;
            }
        });

        friends = new JButton("Friends");
        friends.addActionListener((ActionEvent e) -> {
            TableInfo t = new TableInfo(currentUser.friends());
            if(t.columns != null) {
                replaceTable(userTable, userTableModel, t.rowData, t.columns);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "You have no friends :(");
            }
        });

        ownedGames = new JButton("My Games");
        ownedGames.addActionListener((ActionEvent e) -> {
            TableInfo t = new TableInfo(currentUser.games());
            if (t.columns != null)
                replaceTable(userTable, userTableModel, t.rowData, t.columns);
            else
                JOptionPane.showMessageDialog(frame,
                        "You have no games :(");
        });

        addFriendButton = new JButton("Add");
        addFriendButton.addActionListener((ActionEvent e)->{
            User friend = User.getUserByProfileName(addFriendText.getText());

            //if the username exists and you're not already friends
            if(User.all().contains(friend) && !currentUser.friends().contains(friend)) {
                currentUser.addFriend(friend.id);
            }
        });

        removeFriend = new JButton("Remove Selected Friend");
        removeFriend.addActionListener((ActionEvent e)->{
            int row = userTable.getSelectedRow();
            String friendName = (String) userTable.getValueAt(row,1);
            int friendId = User.getUserByProfileName(friendName).id;
            currentUser.removeFriend(friendId);
            friends.doClick();
        });
    }
}
