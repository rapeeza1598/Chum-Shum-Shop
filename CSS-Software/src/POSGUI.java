import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class POSGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, checkoutButton,clearButton ;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem addItemMenuItem;
    private JMenuItem showItemsMenuItem;
    private JMenuItem clearAllItemsMenuItem;
    private JMenuItem exitMenuItem;
    public void createGUI() {
        frame = new JFrame("Point of Sale System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        // Create the menu bar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        addItemMenuItem = new JMenuItem("Add Item");
        showItemsMenuItem = new JMenuItem("Show Items");
        clearAllItemsMenuItem = new JMenuItem("Search Items");
        exitMenuItem = new JMenuItem("Exit");
        // Create a panel for the clear button
        JPanel clearPanel = new JPanel(new FlowLayout());
        clearButton = new JButton("Clear Table");
        clearPanel.add(clearButton);

        fileMenu.add(addItemMenuItem);
        fileMenu.add(showItemsMenuItem);
        fileMenu.add(clearAllItemsMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        // Create a panel for the search field and search button
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Create a table for displaying items
        String[] columnNames = {"Item ID", "Name", "Price", "Quantity"};
//        Object[][] data = {
//                {1, "Item 1", 10.0, 5},
//                {2, "Item 2", 20.0, 3},
//                {3, "Item 3", 30.0, 2},
//                {4, "Item 4", 40.0, 1},
//                {5, "Item 5", 50.0, 3}
//        };
//        Object[][] data = {};
//        table = new JTable(data, columnNames);
        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        tableModel = new DefaultTableModel(columnNames, 0);
        table.setModel(tableModel);
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(table);

        // Create a panel for the checkout button and clear button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        checkoutButton = new JButton("Checkout");
        clearButton = new JButton("Clear Table");
        buttonPanel.add(checkoutButton);
        buttonPanel.add(clearButton);

        // Add the components to the frame
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add listeners to the search button and checkout button
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                System.out.println(searchText);
                ConnectSqlite connectSqlite = null;
                try {
                    connectSqlite = new ConnectSqlite("database.db");
                } catch (ClassNotFoundException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    connectSqlite.readProduct(searchText);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                Object[] row = {1, searchText, 100, 1};
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(row);
            }
        });
        JScrollPane finalScrollPane = scrollPane;
        checkoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                double totalPrice = 0.0;
                for (int row : selectedRows) {
                    int itemId = (int) table.getModel().getValueAt(row, 0);
                    int quantity = (int) table.getModel().getValueAt(row, 3);
                    double price = (double) table.getModel().getValueAt(row, 2);
                    totalPrice += price * quantity;
                    table.getModel().setValueAt(0, row, 3);
                }
                JOptionPane.showMessageDialog(frame, "Total price: $" + totalPrice);
                //after checkout, remove the selected rows
//                DefaultTableModel model = (DefaultTableModel) table.getModel();
//                for (int i = selectedRows.length - 1; i >= 0; i--) {
//                    model.removeRow(selectedRows[i]);
//                }
                tableModel.setRowCount(0); // Remove all rows
                finalScrollPane.repaint();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0); // Remove all rows
                finalScrollPane.repaint();
            }
        });
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // Add listener to the add item menu item
        addItemMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a dialog for adding an item
                JDialog addItemDialog = new JDialog(frame, "Add Item", true);
                addItemDialog.setSize(300, 200);
                addItemDialog.setLayout(new GridLayout(5, 2));
                addItemDialog.setLocationRelativeTo(null);

                // Add labels and fields for item information
                JLabel nameLabel = new JLabel("Name:");
                JTextField nameField = new JTextField();
                JLabel descriptionLabel = new JLabel("Description:");
                JTextField descriptionField = new JTextField();
                JLabel priceLabel = new JLabel("Price:");
                JTextField priceField = new JTextField();
                JLabel categoryLabel = new JLabel("Category:");
                JTextField categoryField = new JTextField();

                // Add buttons for adding and canceling
                JButton addButton = new JButton("Add");
                JButton cancelButton = new JButton("Cancel");

                // Add components to the dialog
                addItemDialog.add(nameLabel);
                addItemDialog.add(nameField);
                addItemDialog.add(descriptionLabel);
                addItemDialog.add(descriptionField);
                addItemDialog.add(priceLabel);
                addItemDialog.add(priceField);
                addItemDialog.add(categoryLabel);
                addItemDialog.add(categoryField);
                addItemDialog.add(addButton);
                addItemDialog.add(cancelButton);

                // Add listener to the add button
                addButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String name = nameField.getText();
                        String description = descriptionField.getText();
                        double price = Double.parseDouble(priceField.getText());
                        String category = categoryField.getText();
                        ConnectSqlite connectSqlite = null;
                        try {
                            connectSqlite = new ConnectSqlite("database.db");
                        } catch (ClassNotFoundException | SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            connectSqlite.createProduct(name, description, price, category);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        addItemDialog.dispose();
                    }
                });

                // Add listener to the cancel button
                cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addItemDialog.dispose();
                    }
                });
                // Show the dialog
                addItemDialog.setVisible(true);
            }
        });
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        POSGUI gui = new POSGUI();
        gui.createGUI();
    }
}
