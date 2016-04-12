import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox dropdown;
    private JTabbedPane userScreen;
    private JButton friends;
    private JButton ownedGames;
    private JTable userTable;
    private DefaultTableModel tableModel2;

    private User currentUser;

    public DatabaseFrame() {
        frame = new JFrame("Database Frame");
        panel = new JPanel();
        descriptionLabel = new JLabel("Welcome to the DB");

        // Various creation operations
        createDropdown();
        createButtons();
        createTable();
        createUserScreen();
        createLoginScreen();

        // Setup JFrame
        frame.setSize(500, 550);
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
        JLabel userNotFoundWarning = new JLabel("User not found!");
        userNotFoundWarning.setForeground(Color.red);
        userNotFoundWarning.setVisible(false);

        // Create Inputs
        JTextField userNameInput = new JTextField(15);
        JPasswordField passInput = new JPasswordField(15);

        // Button Functionality
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentUser = User.getUserByProfileName(userNameInput.getText());
                if(currentUser != null) {
                    loginScreen.setVisible(false);
                    userGreeting.setText("Hello " + currentUser.profileName);
                    userScreen.setVisible(true);
                }
                else {
                    userNotFoundWarning.setVisible(true);
                }
            }
        });

        loginScreen.add(userLabel);
        loginScreen.add(userNameInput);

        loginScreen.add(passLabel);
        loginScreen.add(passInput);

        loginScreen.add(loginButton);
        loginScreen.add(userNotFoundWarning);
        loginScreen.setLayout(new BoxLayout(loginScreen, BoxLayout.PAGE_AXIS));
    }

    private void createUserScreen() {
        userScreen = new JTabbedPane();

        // Store Panel
        JPanel card1 = new JPanel();

        JPanel optionPanel = new JPanel();
        optionPanel.add(descriptionLabel);
        optionPanel.add(dropdown);
        optionPanel.add(goButton);
        optionPanel.add(purchaseButton);
        optionPanel.setLayout(new FlowLayout());

        card1.add(optionPanel);
        card1.add(new JScrollPane(table));

        card1.setLayout(new BoxLayout(card1, BoxLayout.Y_AXIS));

        // User Page
        JPanel card2 = new JPanel();
        userGreeting = new JLabel("Hello Unknown User");
        card2.add(userGreeting);
        card2.add(friends);
        card2.add(ownedGames);
        card2.add(new JScrollPane(userTable));
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
        table = new JTable();
        userTable = new JTable();
        tableModel = new DefaultTableModel(0, 0);
        tableModel2 = new DefaultTableModel(0, 0);
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

    private void createButtons() {
        goButton = new JButton("Go!");
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String operation = (String) dropdown.getSelectedItem();
                switch(operation) {
                    case("Show All Games"): {
                        TableInfo t = new TableInfo(Game.getAllGames());
                        replaceTable(table,tableModel,t.rowData, t.columns);
                        purchaseButton.setVisible(true);
                        break;
                    }
                    case("Show All Categories") : {
                        TableInfo t = new TableInfo(Category.getAllCategories());
                        replaceTable(table,tableModel, t.rowData, t.columns);
                        purchaseButton.setVisible(false);
                        break;
                    }

                }
            }
        });


        purchaseButton = new JButton("Purchase");
        purchaseButton.setVisible(false);
        purchaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                int GameId = (Integer) table.getValueAt(row,1);

                ErrorCode errorCode = currentUser.purchaseGame(GameId);
                if(errorCode.result == ErrorResult.FAIL)

                if (row == -1) {
                    return;
                }
            }
        });

        friends = new JButton("Friends");
        friends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableInfo t = new TableInfo(currentUser.friends());
                if(t.columns != null) {
                    replaceTable(userTable, tableModel2, t.rowData, t.columns);
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "You have no friends :(");
                }
            }
        });

        ownedGames = new JButton("My Games");
        ownedGames.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableInfo t = new TableInfo(currentUser.games());
                if (t.columns != null)
                    replaceTable(userTable, tableModel2, t.rowData, t.columns);
                else
                    JOptionPane.showMessageDialog(frame,
                            "You have no games :(");
            }
        });
    }
}
