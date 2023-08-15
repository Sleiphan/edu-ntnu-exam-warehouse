package registry;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

public class AWTInterface implements ActionListener, ListSelectionListener, DocumentListener {
    private static final String BUTTON_TEXT_PRINT_ALL = "Print all to console";
    private static final String BUTTON_TEXT_DELETE = "Delete";
    private static final String BUTTON_TEXT_NEW = "New";
    private static final String BUTTON_TEXT_EDIT = "edit";
    private static final String BUTTON_TEXT_ADD = "Add";
    private static final String BUTTON_TEXT_WITHDRAW = "Withdraw";

    private final JFrame frame = new JFrame("Item Registry");
    private final ItemRegistry registry;



    private final JPanel editPanel        = new JPanel();

    private final JLabel itemNumber       = new JLabel();
    private final JLabel description      = new JLabel();
    private final JLabel amountInStorage  = new JLabel();
    private final JLabel price            = new JLabel();
    private final JLabel priceDiscount    = new JLabel();
    private final JLabel category         = new JLabel();
    private final JLabel brand            = new JLabel();
    private final JLabel weight           = new JLabel();
    private final JLabel width            = new JLabel();
    private final JLabel length           = new JLabel();
    private final JLabel color            = new JLabel();
    private final JButton b_edit_description  = new JButton(BUTTON_TEXT_EDIT);
    private final JButton b_edit_price        = new JButton(BUTTON_TEXT_EDIT);
    private final JButton b_edit_discount     = new JButton(BUTTON_TEXT_EDIT);
    private final JButton b_add               = new JButton(BUTTON_TEXT_ADD);
    private final JButton b_withdraw          = new JButton(BUTTON_TEXT_WITHDRAW);

    private final JPanel selectionPanel = new JPanel();
    private final JTextField searchField = new JTextField("");
    private final JRadioButton searchByItemNumber = new JRadioButton("Item number");
    private final JRadioButton searchByDescription = new JRadioButton("Description");
    private final ButtonGroup searchByButtonGroup = new ButtonGroup();
    private final JList<Item> list = new JList<>();
    private final JButton b_printAll = new JButton(BUTTON_TEXT_PRINT_ALL);
    private final JButton b_delete = new JButton(BUTTON_TEXT_DELETE);
    private final JButton b_new = new JButton(BUTTON_TEXT_NEW);



    public AWTInterface(ItemRegistry registry) {
        this.registry = registry;

        list.setListData(registry.getAll());
    }

    public void run() {
        addThisClassAsEventListener();
        setupSelectionPanel();
        setupEditPanel();

        frame.setLayout(new GridLayout(1, 2));
        frame.add(editPanel);
        frame.add(selectionPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 284);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.toFront();
        frame.requestFocus();
    }

    /**
     * Adds this class as an EventListener to all relevant components.
     */
    private void addThisClassAsEventListener() {
        b_edit_description.addActionListener(this);
        b_edit_price.addActionListener(this);
        b_edit_discount.addActionListener(this);
        b_add.addActionListener(this);
        b_withdraw.addActionListener(this);
        searchField.getDocument().addDocumentListener(this);
        searchByDescription.addActionListener(this);
        searchByItemNumber.addActionListener(this);
        list.addListSelectionListener(this);
        b_printAll.addActionListener(this);
        b_delete.addActionListener(this);
        b_new.addActionListener(this);
    }

    /**
     * Performs the necessary setup of the components in the selection panel (right side of the window). There is a lot of layout specification code, so this process deserves its own method.
     */
    private void setupSelectionPanel() {
        // Label the search field
        searchField.setName("Search:");
        // Add the radio buttons to the radio button group
        searchByButtonGroup.add(searchByDescription);
        searchByButtonGroup.add(searchByItemNumber);
        // Set initial state of the radio buttons
        searchByDescription.setSelected(true);
        searchByItemNumber.setSelected(false);
        // Only allow the user to select a single item from the list at once
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add the JList to a JScrollPane to make the list scrollable
        JScrollPane sp = new JScrollPane();
        sp.setViewportView(list);


        // Setup layout for all components in the selection panel
        GridBagLayout selectionLayout = new GridBagLayout();
        GridBagConstraints searchField_c = new GridBagConstraints();
        searchField_c.fill = GridBagConstraints.HORIZONTAL;
        searchField_c.weightx = 1;
        searchField_c.weighty = 0;
        searchField_c.gridx = 0;
        searchField_c.gridy = 0;
        GridBagConstraints searchByDescription_c = new GridBagConstraints();
        searchByDescription_c.fill = GridBagConstraints.HORIZONTAL;
        searchByDescription_c.anchor = GridBagConstraints.LINE_END;
        searchByDescription_c.weightx = 0;
        searchByDescription_c.weighty = 0;
        searchByDescription_c.gridx = 1;
        searchByDescription_c.gridy = 0;
        GridBagConstraints searchByItemNumber_c = new GridBagConstraints();
        searchByItemNumber_c.fill = GridBagConstraints.HORIZONTAL;
        searchByItemNumber_c.anchor = GridBagConstraints.LINE_END;
        searchByItemNumber_c.weightx = 0;
        searchByItemNumber_c.weighty = 0;
        searchByItemNumber_c.gridx = 2;
        searchByItemNumber_c.gridy = 0;
        GridBagConstraints list_c = new GridBagConstraints();
        list_c.fill = GridBagConstraints.BOTH;
        list_c.gridx = 0;
        list_c.gridy = 1;
        list_c.gridwidth = 3;
        list_c.gridheight = 1;
        list_c.weightx = 0;
        list_c.weighty = 1;
        GridBagConstraints printAll_c = new GridBagConstraints();
        printAll_c.fill = GridBagConstraints.HORIZONTAL;
        printAll_c.gridx = 2;
        printAll_c.gridy = 2;
        printAll_c.gridwidth = 1;
        printAll_c.gridheight = 1;
        printAll_c.weightx = 0;
        printAll_c.weighty = 0;
        GridBagConstraints delete_c = (GridBagConstraints) printAll_c.clone();
        delete_c.gridx = 1;
        GridBagConstraints new_c = (GridBagConstraints) printAll_c.clone();
        new_c.gridx = 0;

        // Add all components to the selection panel
        selectionPanel.setLayout(selectionLayout);
        selectionPanel.add(searchField, searchField_c);
        selectionPanel.add(searchByDescription, searchByDescription_c);
        selectionPanel.add(searchByItemNumber, searchByItemNumber_c);
        selectionPanel.add(sp, list_c);
        selectionPanel.add(b_printAll, printAll_c);
        selectionPanel.add(b_delete, delete_c);
        selectionPanel.add(b_new, new_c);
    }

    /**
     * Performs necessary setup of the edit panel (left side of the window).
     */
    private void setupEditPanel() {
        JLabel[] allFields = new JLabel[] {
                itemNumber     ,
                description    ,
                amountInStorage,
                price          ,
                priceDiscount  ,
                category       ,
                brand          ,
                weight         ,
                width          ,
                length         ,
                color
        };

        JLabel[] allLabels = new JLabel[] {
                new JLabel("Item number:"),
                new JLabel("Description:"),
                new JLabel("Amount in storage:"),
                new JLabel("Price:"),
                new JLabel("Price discount:"),
                new JLabel("Category:"),
                new JLabel("Brand:"),
                new JLabel("Weight:"),
                new JLabel("Width:"),
                new JLabel("Length:"),
                new JLabel("Color:"),
        };

        GridBagConstraints label_c = new GridBagConstraints();
        label_c.fill = GridBagConstraints.HORIZONTAL;
        label_c.weightx = 0;
        label_c.weighty = 1;
        label_c.gridx = 0;
        label_c.gridy = 0;
        label_c.gridwidth = 1;

        GridBagConstraints field_c = new GridBagConstraints();
        field_c.fill = GridBagConstraints.HORIZONTAL;
        field_c.weightx = 1;
        field_c.weighty = 1;
        field_c.gridx = 1;
        field_c.gridy = 0;
        field_c.gridwidth = 1;

        editPanel.setLayout(new GridBagLayout());
        for (int i = 0; i < allFields.length; i++) {
            allFields[i].setPreferredSize(new Dimension(150,20));
            editPanel.add(allLabels[i], label_c);
            editPanel.add(allFields[i], field_c);
            label_c.gridy++;
            field_c.gridy++;
        }

        GridBagConstraints edit_c = (GridBagConstraints) field_c.clone();
        edit_c.weightx = 0;
        edit_c.gridx = 2;
        edit_c.gridy = 1;
        edit_c.gridwidth = 2;
        editPanel.add(b_edit_description, edit_c);
        edit_c.gridwidth = 1;
        edit_c.gridy = 2;
        editPanel.add(b_add, edit_c);
        edit_c.gridx = 3;
        editPanel.add(b_withdraw, edit_c);
        edit_c.gridwidth = 2;
        edit_c.gridx = 2;
        edit_c.gridy = 3;
        editPanel.add(b_edit_price, edit_c);
        edit_c.gridy = 4;
        editPanel.add(b_edit_discount, edit_c);


    }



    private void buttonCreateNew() {
        NewItemDialog dialog = new NewItemDialog(registry, new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                updateList();
            }
        });
        dialog.run();
    }

    private void buttonDelete() {
        if (getSelectedItem() == null) {
            JOptionPane.showMessageDialog(frame, "Select which item you want to delete from the list on the right", "No item selected", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        registry.deleteItemEntry(getSelectedItem().getItemNumber());
        updateList();
    }

    private void buttonAdd() {
        if (getSelectedItem() == null) {
            JOptionPane.showMessageDialog(frame, "Select which item you want to increase the amount of from the list on the right", "No item selected", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        Integer numItems = numberDialogInt("Add items", "Enter the amount of items to check in", 0);
        if (numItems == null)
            return;

        registry.increaseAmountInStorage(getSelectedItem().getItemNumber(), numItems);
        updateList();
    }

    private void buttonWithdraw() {
        if (getSelectedItem() == null) {
            JOptionPane.showMessageDialog(frame, "Select which item you want to register withdrawal of from the list on the right", "No item selected", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        Integer numItems;
        while (true) {
            numItems = numberDialogInt("Withdraw items", "Enter the amount of items to withdraw", 0);
            if (numItems == null)
                return;

            int storedItems = registry.getItem(getSelectedItem().getItemNumber()).getAmountInStorage();
            if (storedItems - numItems < 0) {
                JOptionPane.showMessageDialog(frame, "Cannot withdraw "+numItems+" items: not enough items available", "Not enough items", JOptionPane.PLAIN_MESSAGE);
                continue;
            }

            break;
        }

        registry.decreaseAmountInStorage(getSelectedItem().getItemNumber(), numItems);
        updateList();
    }

    private void buttonEditDescription() {
        if (getSelectedItem() == null) {
            JOptionPane.showMessageDialog(frame, "Select which item you want to edit from the list on the right", "No item selected", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        String description;
        while (true) {
            description = JOptionPane.showInputDialog(frame, "Enter the new description for this item", "Change item description", JOptionPane.PLAIN_MESSAGE);
            if (description == null)
                return;

            if (description.isBlank()) {
                JOptionPane.showMessageDialog(frame, "Item description cannot be empty", "Wrong input", JOptionPane.PLAIN_MESSAGE);
                continue;
            }

            break;
        }

        registry.setItemDescription(getSelectedItem().getItemNumber(), description);
        updateList();
    }

    private void buttonEditPrice() {
        if (getSelectedItem() == null) {
            JOptionPane.showMessageDialog(frame, "Select which item you want to edit from the list on the right", "No item selected", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        Integer price = numberDialogInt("Set new item price", "Enter the new price for this item", getSelectedItem().getPrice());
        if (price == null)
            return;

        registry.setItemPrice(getSelectedItem().getItemNumber(), price);
        updateList();
    }

    private void buttonEditDiscount() {
        if (getSelectedItem() == null) {
            JOptionPane.showMessageDialog(frame, "Select which item you want to edit from the list on the right", "No item selected", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        Float discount;
        while (true) {
            discount = numberDialogFloat("Set item discount", "Enter the discount for this item (in %)", getSelectedItem().getPriceDiscount());
            if (discount == null)
                return;

            if (discount > 100.0f) {
                JOptionPane.showMessageDialog(frame, "Item discount has to be a positive number between 0% and 100%", "Invalid discount", JOptionPane.PLAIN_MESSAGE);
                continue;
            }

            break;
        }

        registry.setItemDiscount(getSelectedItem().getItemNumber(), discount);
        updateList();
    }

    private void updateList() {
        int index = list.getSelectedIndex();
        list.setListData(registry.getAll());
        list.setSelectedIndex(index);
    }

    private Integer numberDialogInt(String title, String message, int initialValue) {
        while (true) {
            String num_S = (String) JOptionPane.showInputDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE, null, null, initialValue);

            if (num_S == null) {
                return null;
            }

            int num;
            try {
                num = Integer.parseInt(num_S);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Input not recognized as a whole number", "Wrong input", JOptionPane.PLAIN_MESSAGE);
                continue;
            }

            if (num < 0) {
                JOptionPane.showMessageDialog(frame, "Input must be a positive number", "Wrong input", JOptionPane.PLAIN_MESSAGE);
                continue;
            }

            return num;
        }
    }

    private Float numberDialogFloat(String title, String message, float initialValue) {
        while (true) {
            String num_S = (String) JOptionPane.showInputDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE, null, null, initialValue);

            if (num_S == null) {
                return null;
            }

            float num;
            try {
                num = Float.parseFloat(num_S);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Input not recognized as a number", "Wrong input", JOptionPane.PLAIN_MESSAGE);
                continue;
            }

            if (num < 0) {
                JOptionPane.showMessageDialog(frame, "Input must be a positive number", "Wrong input", JOptionPane.PLAIN_MESSAGE);
                continue;
            }

            return num;
        }
    }

    private Item getSelectedItem() {
        return list.getSelectedValue();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton b) {
            if (b.equals((b_delete)))
                buttonDelete();
            else if (b.equals((b_new)))
                buttonCreateNew();
            else if (b.equals((b_printAll))) {
                System.out.println();
                registry.printAllEntries();
            } else if (b.equals(b_edit_description))
                buttonEditDescription();
            else if (b.equals(b_edit_price))
                buttonEditPrice();
            else if (b.equals(b_edit_discount))
                buttonEditDiscount();
            else if (b.equals(b_add))
                buttonAdd();
            else if (b.equals(b_withdraw))
                buttonWithdraw();
        }

        if (e.getSource() instanceof JRadioButton rb)
            if (rb.equals(searchByDescription) || rb.equals(searchByItemNumber))
                searchField.setText(searchField.getText());
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        Item i = getSelectedItem();
        if (i == null) {
            emptyTextFields();
            return;
        }

        itemNumber     .setText(i.getItemNumber());
        description    .setText(i.getDescription());
        amountInStorage.setText(String.valueOf(i.getAmountInStorage()));
        price          .setText(String.valueOf(i.getPriceAfterDiscount()));
        priceDiscount  .setText(String.valueOf(i.getPriceDiscount()));
        category       .setText(String.valueOf(i.getCategory()));
        brand          .setText(i.getBrand());
        weight         .setText(String.valueOf(i.getWeight()));
        width          .setText(String.valueOf(i.getWidth()));
        length         .setText(String.valueOf(i.getLength()));
        color          .setText(i.getColor());
    }



    private void textUpdated(DocumentEvent e) {
        // If this event was triggered by the search field
        if (e.getDocument().equals(searchField.getDocument())) {
            if (searchField.getText() == null || searchField.getText().equals("")) {
                list.setListData(registry.getAll());
                return;
            }

            // Perform the search
            String[] results;
            if (searchByDescription.isSelected())
                results = registry.searchByDescription(searchField.getText());
            else
                results = registry.searchByItemNumber(searchField.getText());

            // If no results, empty the selection list
            if (results == null) {
                list.setListData(new Item[0]);
                return;
            }

            // Extract the all resulting Items from the registry
            Item[] hits = registry.getItems(results);

            // Update the selection list with the results
            list.setListData(hits);
        }
    }

    private void emptyTextFields() {
        itemNumber     .setText("");
        description    .setText("");
        amountInStorage.setText("");
        price          .setText("");
        priceDiscount  .setText("");
        category       .setText("");
        brand          .setText("");
        weight         .setText("");
        width          .setText("");
        length         .setText("");
        color          .setText("");
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        textUpdated(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        textUpdated(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        textUpdated(e);
    }
}
