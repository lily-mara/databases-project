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
    JButton getAllGames;
    JTable table;
    DefaultTableModel tableModel;

    public DatabaseFrame() {
        frame = new JFrame("Database Frame");
        panel = new JPanel();
        descriptionLabel = new JLabel("Welcome to the DB");
        createButtons();
        creteTable();

        getAllGames.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableInfo t = new TableInfo(Game.getAllGames());
                replaceTable(t.rowData, t.columns);
            }
        });

        frame.setSize(500, 500);
        //frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.WHITE);

        panel.add(descriptionLabel);
        panel.add(getAllGames);
        panel.add(new JScrollPane(table));
        frame.add(panel);

        frame.setVisible(true);
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
        getAllGames = new JButton("Get Games");

    }
}
