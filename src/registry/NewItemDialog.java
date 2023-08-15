package registry;

import registry.ItemCategory;
import registry.ItemRegistry;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public class NewItemDialog implements ActionListener, DocumentListener {

    private static final Color BG_VALID_INPUT = Color.WHITE;
    private static final Color BG_INVALID_INPUT = Color.RED;

    private final JFrame frame = new JFrame("Register new item");
    private final JTextField itemNumber             = new JTextField();
    private final JTextField description            = new JTextField();
    private final JTextField amountInStorage        = new JTextField();
    private final JTextField price                  = new JTextField();
    private final JComboBox<ItemCategory>  category = new JComboBox<>();
    private final JTextField brand                  = new JTextField();
    private final JTextField weight                 = new JTextField();
    private final JTextField width                  = new JTextField();
    private final JTextField length                 = new JTextField();
    private final JTextField color                  = new JTextField();
    private final JComponent[] allInputComponents = new JComponent[] { itemNumber, description, amountInStorage, price, category, brand, weight, width, length, color };
    private final JLabel[] allLabels = new JLabel[] {
            new JLabel("Item number:"),
            new JLabel("Description:"),
            new JLabel("Amount in storage:"),
            new JLabel("Price:"),
            new JLabel("Category:"),
            new JLabel("Brand:"),
            new JLabel("Weight:"),
            new JLabel("Width:"),
            new JLabel("Length:"),
            new JLabel("Color:")
    };

    private final JButton buttonSave = new JButton("Save");
    private final JButton buttonCancel = new JButton("Cancel");

    private final ItemRegistry registry;

    public NewItemDialog(ItemRegistry registry, WindowListener windowListener) {
        this.registry = registry;
        frame.addWindowListener(windowListener);
    }

    public void run() {
        setupGUI();
    }

    private void setupGUI() {
        for (ItemCategory c : ItemCategory.values())
            category.addItem(c);

        buttonSave.addActionListener(this);
        buttonCancel.addActionListener(this);
        category.addActionListener(this);
        for (JComponent c : allInputComponents)
            if (c instanceof JTextField) {
                ((JTextField)c).getDocument().addDocumentListener(this);
                ((JTextField)c).setText("");
            }


        GridBagConstraints textField_c = new GridBagConstraints();
        textField_c.fill = GridBagConstraints.HORIZONTAL;
        textField_c.gridx = 1;
        textField_c.gridy = 0;
        textField_c.weightx = 1;
        GridBagConstraints label_c = (GridBagConstraints) textField_c.clone();
        label_c.gridx = 0;
        label_c.weightx = 0;

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        for (int i = 0; i < allInputComponents.length; i++) {
            frame.add(allLabels[i], label_c);
            frame.add(allInputComponents[i], textField_c);
            label_c.gridy++;
            textField_c.gridy++;
        }

        frame.add(new JLabel(""), label_c);
        frame.add(new JLabel(""), textField_c);
        label_c.gridy++;
        textField_c.gridy++;
        frame.add(buttonSave, label_c);
        frame.add(buttonCancel, textField_c);

        frame.setSize(400, 280);
        frame.setVisible(true);
    }

    private void cancel() {
        frame.dispose();
    }

    private void save() {
        if (!hasValidInput(itemNumber     ) ||
            !hasValidInput(description    ) ||
            !hasValidInt  (amountInStorage) ||
            !hasValidInt  (price          ) ||
            !hasValidInput(brand          ) ||
            !hasValidFloat(weight         ) ||
            !hasValidFloat(width          ) ||
            !hasValidFloat(length         ) ||
            !hasValidInput(color          ))
        {
            JOptionPane.showMessageDialog(frame, "Some fields contain invalid input", "Invalid input", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        if (registry.itemNumberTaken(itemNumber.getText())) {
            JOptionPane.showMessageDialog(frame, "That item number is already in use", "Item number is not unique", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        registry.registerNewItem(
                itemNumber.getText(),
                description.getText(),
                Integer.parseInt(amountInStorage.getText()),
                Integer.parseInt(price.getText()),
                (ItemCategory) category.getSelectedItem(),
                brand.getText(),
                Float.parseFloat(weight.getText()),
                Float.parseFloat(width.getText()),
                Float.parseFloat(length.getText()),
                color.getText()
                );

        frame.dispose();
    }

    private static boolean hasValidInt(JTextField textField) {
        if (!hasValidInput(textField))
            return false;

        try {
            return Integer.parseInt(textField.getText()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean hasValidFloat(JTextField textField) {
        if (!hasValidInput(textField))
            return false;
        try {
            return Float.parseFloat(textField.getText()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean hasValidInput(JTextField textField) {
        String s = textField.getText();
        return s != null && !s.isBlank();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton b) {
            if (b.equals(buttonSave))
                save();
            else if (b.equals(buttonCancel))
                cancel();
        }
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

    private void textUpdated(DocumentEvent e) {
        JTextField src = null;
        for (JComponent c : allInputComponents) {
            if (!(c instanceof JTextField current))
                continue;

            if (current.getDocument().equals(e.getDocument())) {
                src = current;
                break;
            }
        }

        assert src != null; // Because src can only be equal to a value within the array 'allInputComponents'.
        if (src.equals(itemNumber) || src.equals(description) || src.equals(brand) || src.equals(color)) {
            if (hasValidInput(src))
                src.setBackground(BG_VALID_INPUT);
            else
                src.setBackground(BG_INVALID_INPUT);

            if (src.equals(itemNumber) && registry.itemNumberTaken(itemNumber.getText()))
                itemNumber.setBackground(BG_INVALID_INPUT);
        }

        else if (src.equals(weight) || src.equals(width) || src.equals(length)) {
            if (hasValidFloat(src))
                src.setBackground(BG_VALID_INPUT);
            else
                src.setBackground(BG_INVALID_INPUT);
        }

        else if (src.equals(price) || src.equals(amountInStorage)) {
            if (hasValidInt(src))
                src.setBackground(BG_VALID_INPUT);
            else
                src.setBackground(BG_INVALID_INPUT);
        }
    }
}
