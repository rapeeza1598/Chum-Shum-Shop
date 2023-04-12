import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

public class POSGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton, checkoutButton, clearButton, editQuantityAndPriceButton;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem addItemMenuItem;
    private JMenuItem showItemsMenuItem;
    private JMenuItem oderItemMenu;
    private JMenuItem exitMenuItem;

    public void createGUI() {
        frame = new JFrame("Point of Sale System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 720);
        frame.setLocationRelativeTo(null);
        frame.setFont(new Font("Tahoma", Font.PLAIN, 16));
        // Create the menu bar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        addItemMenuItem = new JMenuItem("Add Item");
        showItemsMenuItem = new JMenuItem("Show Items");
        oderItemMenu = new JMenuItem("Order Item");
        exitMenuItem = new JMenuItem("Exit");
        // Create a panel for the clear button
        JPanel clearPanel = new JPanel(new FlowLayout());
        clearButton = new JButton("Clear Table");
        clearPanel.add(clearButton);

        fileMenu.add(addItemMenuItem);
        fileMenu.add(showItemsMenuItem);
        fileMenu.add(oderItemMenu);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        // Create a panel for the search field and search button
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchField.setFont(new Font("Tahoma", Font.PLAIN, 16));
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
        scrollPane.setFont(new Font("Tahoma", Font.PLAIN, 16));

        // Create a panel for the checkout button and clear button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        checkoutButton = new JButton("Checkout");
        clearButton = new JButton("Clear Table");
        editQuantityAndPriceButton = new JButton("Edit Quantity");
        buttonPanel.add(checkoutButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(editQuantityAndPriceButton);
        // Add the components to the frame
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        ConnectSqlite connectSqlite = null;
        try {
            connectSqlite = new ConnectSqlite("database.db");
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException(ex);
        }

        // Add listeners to the search button and checkout button
        ConnectSqlite finalConnectSqlite = connectSqlite;

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                System.out.println(searchText);
                try {
                    Object[] row = (Object[]) finalConnectSqlite.readProduct(searchText);
                    if (row == null) {
                        JOptionPane.showMessageDialog(frame, "Item not found");
                    } else {
                        // Add the item to the table
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.addRow(row);
                        searchField.setText("");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
//                Object[] row = {1, searchText, 1, 1};
//                DefaultTableModel model = (DefaultTableModel) table.getModel();
//                model.addRow(row);
            }
        });
        // searchField enter key listener
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                System.out.println(searchText);
                try {
                    Object[] row = (Object[]) finalConnectSqlite.readProduct(searchText);
                    if (row == null) {
                        JOptionPane.showMessageDialog(frame, "Item not found");
                    } else {
                        // Add the item to the table
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.addRow(row);
                        searchField.setText("");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
//                Object[] row = {1, searchText, 1, 1};
//                DefaultTableModel model = (DefaultTableModel) table.getModel();
//                model.addRow(row);
            }
        });
        JScrollPane ScrollPane = scrollPane;
        // Add listeners to the edit quantity and edit price buttons to table
        editQuantityAndPriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // not selected
                if (table.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select an item to edit");
                } else {
                    int[] selectedRows = table.getSelectedRows();
                    for (int row : selectedRows) {
                        int itemId = (int) table.getModel().getValueAt(row, 0);
                        int quantity = (int) table.getModel().getValueAt(row, 3);
                        String name = (String) table.getModel().getValueAt(row, 1);
                        double price = Double.parseDouble((String) table.getModel().getValueAt(row, 2));
                        String quantityString = (String) JOptionPane.showInputDialog(frame, name, "Edit Quantity", JOptionPane.QUESTION_MESSAGE);
                        if (quantityString != null && quantityString.length() > 0) {
                            int newQuantity = Integer.parseInt(quantityString);
                            table.getModel().setValueAt(newQuantity, row, 3);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Please enter a valid quantity");
                        }
                    }
                }
            }
        });
        // Add listeners to the checkout button to calculate the total price all items in the table
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get all the rows
                int rowCount = table.getRowCount();
                double totalPrice = 0.0;
                for (int row = 0; row < rowCount; row++) {
                    int itemId = (int) table.getModel().getValueAt(row, 0);
                    int quantity = (int) table.getModel().getValueAt(row, 3);
                    double price = Double.parseDouble((String) table.getModel().getValueAt(row, 2));
                    totalPrice += price * quantity;
                }
                //randomly generate an order id
                Random rand = new Random();
                int orderId = rand.nextInt(1000000);
                //insert the order into the database
                try {
                    finalConnectSqlite.insertOrder(orderId, totalPrice);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                //insert the order items into the database
                for (int row = 0; row < rowCount; row++) {
                    int itemId = (int) table.getModel().getValueAt(row, 0);
                    int quantity = (int) table.getModel().getValueAt(row, 3);
                    double price = Double.parseDouble((String) table.getModel().getValueAt(row, 2));
                    try {
                        finalConnectSqlite.insertOrderItem(orderId, itemId, quantity, price);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                JOptionPane.showMessageDialog(frame, "Total price: $" + totalPrice);
                //after checkout, remove all the rows
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
            }
        });
//        checkoutButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // not selected
//                if (table.getSelectedRow() == -1) {
//                    JOptionPane.showMessageDialog(frame, "Please select an item to checkout");
//                } else {
//                    int[] selectedRows = table.getSelectedRows();
//                    double totalPrice = 0.0;
//                    for (int row : selectedRows) {
//                        int itemId = (int) table.getModel().getValueAt(row, 0);
//                        int quantity = (int) table.getModel().getValueAt(row, 3);
//                        double price = Double.parseDouble((String) table.getModel().getValueAt(row, 2));
//                        totalPrice += price * quantity;
//                    }
//                    JOptionPane.showMessageDialog(frame, "Total price: $" + totalPrice);
//                }
//                //after checkout, remove the selected rows
////                DefaultTableModel model = (DefaultTableModel) table.getModel();
////                for (int i = selectedRows.length - 1; i >= 0; i--) {
////                    model.removeRow(selectedRows[i]);
////                }
//                tableModel.setRowCount(0); // Remove all rows
//                ScrollPane.repaint();
//            }
//        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0); // Remove all rows
                ScrollPane.repaint();
            }
        });
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        ConnectSqlite finalConnectSqlite1 = connectSqlite;
        showItemsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] showItemsMenuItemColumnNames = {"Item ID", "Name", "Description", "Price", "Category"};
                //Create a dialog for showing items
                JDialog showItemsDialog = new JDialog(frame, "Show Items", true);
                showItemsDialog.setSize(500, 550);
                showItemsDialog.setLayout(new FlowLayout());
                showItemsDialog.setLocationRelativeTo(null);

                //Add table to the dialog
                JLabel showItems = new JLabel("Show Items");
                JTable showItemsMenuItemTable = new JTable();
                JScrollPane showItemsMenuItemScrollPane = new JScrollPane(showItemsMenuItemTable);
                DefaultTableModel showItemsMenuItemTableModel = new DefaultTableModel(showItemsMenuItemColumnNames, 0);
                showItemsMenuItemTable.setModel(showItemsMenuItemTableModel);
                showItemsMenuItemTable.setFillsViewportHeight(true);
                showItemsMenuItemTable.setFont(new Font("Tahoma", Font.PLAIN, 14));
                showItemsMenuItemScrollPane.setFont(new Font("Tahoma", Font.PLAIN, 16));

                //Create a panel for the edit button, delete button and close button
                JPanel buttonPanel = new JPanel(new FlowLayout());
                JButton editButton = new JButton("Edit");
                JButton deleteButton = new JButton("Delete");
                JButton closeButton = new JButton("Close");

                buttonPanel.add(editButton);
                buttonPanel.add(deleteButton);
                buttonPanel.add(closeButton);
                showItemsDialog.add(buttonPanel);

                // Add the components to the frame
                showItemsDialog.add(showItems, BorderLayout.NORTH);
                showItemsDialog.add(showItemsMenuItemScrollPane, BorderLayout.CENTER);
                showItemsDialog.add(buttonPanel, BorderLayout.SOUTH);

                // Get all items from the database
                try {
                    Object[][] rows = finalConnectSqlite1.readAllProducts();
                    for (Object[] row : rows) {
                        showItemsMenuItemTableModel.addRow(row);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get the values from the fields
                        int id = (int) showItemsMenuItemTable.getModel().getValueAt(showItemsMenuItemTable.getSelectedRow(), 0);
                        String name = (String) showItemsMenuItemTable.getModel().getValueAt(showItemsMenuItemTable.getSelectedRow(), 1);
                        String description = (String) showItemsMenuItemTable.getModel().getValueAt(showItemsMenuItemTable.getSelectedRow(), 2);
                        double price = (Double) showItemsMenuItemTable.getModel().getValueAt(showItemsMenuItemTable.getSelectedRow(), 3);
                        String category = (String) showItemsMenuItemTable.getModel().getValueAt(showItemsMenuItemTable.getSelectedRow(), 4);
                        //Create a dialog for editing an item
                        JDialog editItemDialog = new JDialog(frame, "Edit Item", true);
                        editItemDialog.setSize(250, 200);
                        editItemDialog.setLocationRelativeTo(null);
                        editItemDialog.setFont(new Font("Tahoma", Font.PLAIN, 16));
                        JLabel editItem = new JLabel("Edit Item");
                        //Add labels and fields for item information
                        JLabel nameLabel = new JLabel("Name:");
                        JTextField nameField = new JTextField(name);
                        JLabel descriptionLabel = new JLabel("Description:");
                        JTextField descriptionField = new JTextField(description);
                        JLabel priceLabel = new JLabel("Price:");
                        JTextField priceField = new JTextField(String.valueOf(price));
                        JLabel categoryLabel = new JLabel("Category:");
                        JTextField categoryFiend = new JTextField(String.valueOf(String.valueOf(category)));
                        //Add buttons to the dialog
                        JPanel buttonPanel = new JPanel(new FlowLayout());
                        JButton saveButton = new JButton("Save");
                        JButton cancelButton = new JButton("Cancel");
                        buttonPanel.add(saveButton);
                        buttonPanel.add(cancelButton);
                        //Add listener to the save button
                        saveButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                //Get the values from the fields
                                String name = nameField.getText();
                                String description = descriptionField.getText();
                                double price = Double.parseDouble(priceField.getText());
                                String category = (String) categoryFiend.getText();
                                //Update the item in the database
                                try {
                                    ConnectSqlite.updateProduct(id, name, description, price, category);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                //Update the item in the showItemsMenuItemTable
                                showItemsMenuItemTable.getModel().setValueAt(name, showItemsMenuItemTable.getSelectedRow(), 1);
                                showItemsMenuItemTable.getModel().setValueAt(price, showItemsMenuItemTable.getSelectedRow(), 2);
                                showItemsMenuItemTable.getModel().setValueAt(category, showItemsMenuItemTable.getSelectedRow(), 3);
                                showItemsMenuItemTable.getModel().setValueAt(description, showItemsMenuItemTable.getSelectedRow(), 4);
                                //Show a message
                                JOptionPane.showMessageDialog(null, "Item updated successfully");
                                //Close the dialog
                                editItemDialog.dispose();
                            }
                        });
                        //Add listener to the cancel button
                        cancelButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                editItemDialog.dispose();
                            }
                        });
                        JPanel labelPanel = new JPanel(new GridLayout(4, 2));
                        labelPanel.add(nameLabel);
                        labelPanel.add(nameField);
                        labelPanel.add(descriptionLabel);
                        labelPanel.add(descriptionField);
                        labelPanel.add(priceLabel);
                        labelPanel.add(priceField);
                        labelPanel.add(categoryLabel);
                        labelPanel.add(categoryFiend);

                        JPanel editItemPanel = new JPanel(new FlowLayout());
                        editItemPanel.add(editItem);
                        //Add the components to the dialog
                        editItemDialog.add(editItemPanel, BorderLayout.NORTH);
                        editItemDialog.add(labelPanel, BorderLayout.CENTER);
                        editItemDialog.add(buttonPanel, BorderLayout.SOUTH);
                        //Show the dialog
                        editItemDialog.setVisible(true);
                    }
                });

                //Add listener to the delete button
                deleteButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get the id of the selected item
                        int id = (int) showItemsMenuItemTable.getModel().getValueAt(showItemsMenuItemTable.getSelectedRow(), 0);
                        //Delete the item from the database
                        try {
                            ConnectSqlite.deleteProduct(id);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        //Delete the item from the showItemsMenuItemTable
                        showItemsMenuItemTableModel.removeRow(showItemsMenuItemTable.getSelectedRow());
                        //Show a message
                        JOptionPane.showMessageDialog(null, "Item deleted successfully");

                    }
                });
                closeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        showItemsDialog.dispose();
                    }
                });
                //Show the dialog
                showItemsDialog.setVisible(true);
            }
        });
        // order menu item listener for showing all order menu item new dialog table
        oderItemMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Create a dialog for showing all items
                JDialog showOderDialog = new JDialog(frame, "Show Oder", true);
                showOderDialog.setSize(800, 600);
                showOderDialog.setLocationRelativeTo(null);
                showOderDialog.setFont(new Font("Tahoma", Font.PLAIN, 16));
                JLabel showOder = new JLabel("Show Oder");
                //Add a table to the dialog to show all items
                JTable showOderTable = new JTable();
                showOderTable.setFont(new Font("Tahoma", Font.PLAIN, 16));
                showOderTable.setRowHeight(30);
                DefaultTableModel showOderTableModel = new DefaultTableModel();
                showOderTableModel.setColumnIdentifiers(new Object[]{"ID", "Oder ID", "Product Name", "Quantity", "Price", "Total Price"});
                showOderTable.setModel(showOderTableModel);
                //Add buttons to the dialog
                JPanel buttonPanel = new JPanel(new FlowLayout());
                JButton editButton = new JButton("Edit Oder");
                JButton deleteButton = new JButton("Delete Oder");
                JButton closeButton = new JButton("Close");
                buttonPanel.add(editButton);
                buttonPanel.add(deleteButton);
                buttonPanel.add(closeButton);
                //Add a scroll pane to the table
                JScrollPane scrollPane = new JScrollPane(showOderTable);

                // Get all items from the database
                Object[][] rows = new Object[0][];
                try {
                    rows = finalConnectSqlite.getAllOrderItems();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println(Arrays.deepToString(rows));
                for (Object[] row : rows) {
                    showOderTableModel.addRow(row);
                }

                //Add listener to the close button
                closeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        showOderDialog.dispose();
                    }
                });
                //Add listener to the delete button
                deleteButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get the id of the selected item
                        int id = (int) showOderTable.getModel().getValueAt(showOderTable.getSelectedRow(), 0);
                        //Delete the item from the database
                        try {
                            ConnectSqlite.deleteOrderItem(id);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        //Delete the item from the showItemsMenuItemTable
                        showOderTableModel.removeRow(showOderTable.getSelectedRow());
                        //Show a message
                        JOptionPane.showMessageDialog(null, "Oder deleted successfully");

                    }
                });
                //Add listener to the edit button
                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //Get the id of the selected item
                        int id = (int) showOderTable.getModel().getValueAt(showOderTable.getSelectedRow(), 0);
                        //Create a dialog for editing an item
                        JDialog editOderDialog = new JDialog(frame, "Edit Oder", true);
                        editOderDialog.setSize(300, 200);
                        editOderDialog.setLayout(new GridLayout(5, 2));
                        editOderDialog.setLocationRelativeTo(null);
                        editOderDialog.setFont(new Font("Tahoma", Font.PLAIN, 16));
                        //Add labels and fields for item information
                        JLabel customerNameLabel = new JLabel("Customer Name:");
                        JTextField customerNameField = new JTextField();
                        JLabel customerAddressLabel = new JLabel("Customer Address:");
                        JTextField customerAddressField = new JTextField();
                        JLabel customerPhoneLabel = new JLabel("Customer Phone:");
                        JTextField customerPhoneField = new JTextField();
                        JLabel productNameLabel = new JLabel("Product Name:");
                        JTextField productNameField = new JTextField();
                        JLabel productPriceLabel = new JLabel("Product Price:");
                        JTextField productPriceField = new JTextField();
                        JLabel productQuantityLabel = new JLabel("Product Quantity:");
                        JTextField productQuantityField = new JTextField();
                        JLabel totalPriceLabel = new JLabel("Total Price:");
                        JTextField totalPriceField = new JTextField();
                        //Add buttons to the dialog
                        JPanel buttonPanel = new JPanel(new FlowLayout());
                        JButton saveButton = new JButton("Save");
                        JButton cancelButton = new JButton("Cancel");
                        buttonPanel.add(saveButton);
                        buttonPanel.add(cancelButton);
                        //Add listener to the save button
                        saveButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                //Get the values from the fields
                                String customerName = customerNameField.getText();
                                String customerAddress = customerAddressField.getText();
                                String customerPhone = customerPhoneField.getText();
                                String productName = productNameField.getText();
                                double productPrice = Double.parseDouble(productPriceField.getText());
                                int productQuantity = Integer.parseInt(productQuantityField.getText());
                                double totalPrice = Double.parseDouble(totalPriceField.getText());
                                //Update the item in the database
//                                try {
//                                    ConnectSqlite.updateOder(id, customerName, customerAddress, customerPhone, productName, productPrice, productQuantity, totalPrice);
//                                } catch (SQLException throwables) {
//                                    throwables.printStackTrace();
//                                }
                                //Update the item in the showOderTable
                                showOderTableModel.setValueAt(customerName, showOderTable.getSelectedRow(), 1);
                                showOderTableModel.setValueAt(customerAddress, showOderTable.getSelectedRow(), 2);
                                showOderTableModel.setValueAt(customerPhone, showOderTable.getSelectedRow(), 3);
                                showOderTableModel.setValueAt(productName, showOderTable.getSelectedRow(), 4);
                                showOderTableModel.setValueAt(productPrice, showOderTable.getSelectedRow(), 5);
                                showOderTableModel.setValueAt(productQuantity, showOderTable.getSelectedRow(), 6);
                                showOderTableModel.setValueAt(totalPrice, showOderTable.getSelectedRow(), 7);
                                //Show a message
                                JOptionPane.showMessageDialog(null, "Oder updated successfully");
                                //Close the dialog
                                editOderDialog.dispose();
                            }
                        });
                        //Add listener to the cancel button
                        cancelButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                editOderDialog.dispose();
                            }
                        });
                        //Add the components to the dialog
                        editOderDialog.add(customerNameLabel);
                        editOderDialog.add(customerNameField);
                        editOderDialog.add(customerAddressLabel);
                        editOderDialog.add(customerAddressField);
                        editOderDialog.add(customerPhoneLabel);
                        editOderDialog.add(customerPhoneField);
                        editOderDialog.add(productNameLabel);
                        editOderDialog.add(productNameField);
                        editOderDialog.add(productPriceLabel);
                        editOderDialog.add(productPriceField);
                        editOderDialog.add(productQuantityLabel);
                        editOderDialog.add(productQuantityField);
                        editOderDialog.add(totalPriceLabel);
                        editOderDialog.add(totalPriceField);
                        editOderDialog.add(buttonPanel, BorderLayout.SOUTH);
                        //Show the dialog
                        editOderDialog.setVisible(true);
                    }
                });

                //Add the components to the dialog
                showOderDialog.add(showOder, BorderLayout.NORTH);
                showOderDialog.add(scrollPane, BorderLayout.CENTER);
                showOderDialog.add(buttonPanel, BorderLayout.SOUTH);
                //Show the dialog
                showOderDialog.setVisible(true);
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
                addItemDialog.setFont(new Font("Tahoma", Font.PLAIN, 16));

                // Add labels and fields for item information
                JLabel nameLabel = new JLabel("Name:");
                JTextField nameField = new JTextField();
                nameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
                JLabel descriptionLabel = new JLabel("Description:");
                JTextField descriptionField = new JTextField();
                descriptionField.setFont(new Font("Tahoma", Font.PLAIN, 16));
                JLabel priceLabel = new JLabel("Price:");
                JTextField priceField = new JTextField();
                JLabel categoryLabel = new JLabel("Category:");
                JTextField categoryField = new JTextField();
                categoryField.setFont(new Font("Tahoma", Font.PLAIN, 16));

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
                        try {
                            String name = nameField.getText();
                            String description = descriptionField.getText();
                            double price = Double.parseDouble(priceField.getText());
                            String category = categoryField.getText();
                            try {
                                finalConnectSqlite.createProduct(name, description, price, category);
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            // Show a message
                            JOptionPane.showMessageDialog(null, "Item added successfully");
                            addItemDialog.dispose();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Please enter valid data");
                        }
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
