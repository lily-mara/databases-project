import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by loomisdf on 4/10/2016.
 */
public class DatabaseFrame{

    JFrame frame;
    JLabel descriptionLabel;
    JPanel panel;
    JButton goButton;
    JTable table;
    DefaultTableModel tableModel;
    JComboBox dropdown;

    public DatabaseFrame() {
        frame = new JFrame("Database Frame");
        panel = new JPanel();
        descriptionLabel = new JLabel("Welcome to the DB");
        createDropdown();
        createButtons();
        creteTable();



        frame.setSize(500, 500);
        //frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);

        panel.add(descriptionLabel);
        panel.add(dropdown);
        panel.add(goButton);
        panel.add(new JScrollPane(table));
        frame.add(panel);

        frame.setVisible(true);
    }

    private void createDropdown() {
        String[] options = {"Show All Games","User Page","Show All Categories"};
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
