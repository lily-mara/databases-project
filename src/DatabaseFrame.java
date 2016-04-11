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
    private JLabel descriptionLabel;
    private JPanel panel;
    private JPanel loginScreen;
    private JButton goButton;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox dropdown;
    private JTabbedPane userScreen;

    private final static int extraWindowWidth = 100;

    public DatabaseFrame() {
        frame = new JFrame("Database Frame");
        panel = new JPanel();
        descriptionLabel = new JLabel("Welcome to the DB");

        // Various creation operations
        createDropdown();
        createButtons();
        creteTable();
        createUserScreen();
        createLoginScreen();

        // Setup JFrame
        frame.setSize(500, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);

        // Add things to panel
        //panel.add(descriptionLabel);
        //panel.add(dropdown);
        //panel.add(goButton);
        //panel.add(new JScrollPane(table));
        panel.add(userScreen);
        panel.add(loginScreen);

        // Add panel to frame
        frame.add(panel);

        // Set the frame visible
        frame.setVisible(true);
    }

    private void createLoginScreen() {
        loginScreen = new JPanel();

        JLabel userLabel = new JLabel("Uesrname:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userNameInput = new JTextField(15);
        JPasswordField passInput = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginScreen.setVisible(false);
                userScreen.setVisible(true);
            }
        });

        loginScreen.add(userLabel);
        loginScreen.add(userNameInput);

        loginScreen.add(passLabel);
        loginScreen.add(passInput);

        loginScreen.add(loginButton);
        loginScreen.setLayout(new BoxLayout(loginScreen, BoxLayout.PAGE_AXIS));
    }

    private void createUserScreen() {
        userScreen = new JTabbedPane();
        JPanel card1 = new JPanel();

        JPanel optionPanel = new JPanel();
        optionPanel.add(descriptionLabel);
        optionPanel.add(dropdown);
        optionPanel.add(goButton);
        optionPanel.setLayout(new FlowLayout());

        card1.add(optionPanel);
        card1.add(new JScrollPane(table));

        card1.setLayout(new BoxLayout(card1, BoxLayout.Y_AXIS));

        JPanel card2 = new JPanel();
        card2.add(new JLabel("User Page"));

        userScreen.addTab("Store", card1);
        userScreen.addTab("User Page", card2);

        userScreen.setVisible(false);
    }

    private void createDropdown() {
        String[] options = {"Show All Games", "Show All Categories"};
        dropdown = new JComboBox(options);
    }

    private void creteTable() {
        table = new JTable();
        tableModel = new DefaultTableModel(0, 0);
    }

    public void replaceTable(Object rowData[][], Object columns[]) {
        tableModel.setColumnIdentifiers(columns);
        table.setModel(tableModel);

        // Remove Rows
        if (tableModel.getRowCount() > 0) {
            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                tableModel.removeRow(i);
            }
        }

        // Add new rows
        for(int r = 0; r < rowData.length; r++) {
            tableModel.addRow(rowData[r]);
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
                        replaceTable(t.rowData, t.columns);
                        break;
                    }
                    case("Show All Categories") : {
                        TableInfo t = new TableInfo(Category.getAllCategories());
                        replaceTable(t.rowData, t.columns);
                        break;
                    }

                }
            }
        });
    }
}
