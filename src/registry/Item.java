package registry;

import java.util.Formatter;

/**
 * A data structure representing a collection of identical items stored in a warehouse.
 * <br><br> Besides representing an item through attributes like weight and category, this class also contains a value representing the amount of this item, namely <code>amountInStorage</code>.
 * This fits well with most factory warehouses, where the number of similar items exceed the number of dissimilar item.
 */
public class Item {
    private String itemNumber;
    private String description;
    private int amountInStorage;
    private int price;
    private float priceDiscount = 0;
    private ItemCategory category;
    private String brand;
    private float weight;
    private float width;
    private float length;
    private String color;

    /**
     * Creates a new instance of 
     * @param itemNumber A unique identifier for this instance, consisting of numbers and letters
     * @param description A brief description of the item
     * @param amountInStorage The amount of this item present in storage
     * @param price The price of this item
     * @param category The category in which this item fits
     * @param brand The brand associated with this
     * @param weight The weight of this item, and not the combined weight of all items of this type
     * @param width The width of this item
     * @param length The length of this item
     * @param color The color of this item
     */
    public Item(String itemNumber, String description, int amountInStorage, int price, ItemCategory category, String brand, float weight, float width, float length, String color) {
        setItemNumber(itemNumber);
        setDescription(description);
        setAmountInStorage(amountInStorage);
        setPrice(price);
        setCategory(category);
        setBrand(brand);
        setWeight(weight);
        setWidth(width);
        setLength(length);
        setColor(color);
    }

    /**
     * Returns a short string representation of this item, tuned for usage in JList and other awt/swing components. Contains only item number and description in this format: "(description)   [(item number)]"
     * @return A short-form string representation of this .
     */
    @Override
    public String toString() {
        return description+"   ["+itemNumber+"]";
    }

    /**
     * Returns a full string representation of this  with every field presented. Floating point values are presented with two decimals.
     * @return A long-form string representation of this .
     */
    public String toStringFull() {
        final int NUM_DECIMALS = 2;
        return toStringFull(NUM_DECIMALS);
    }

    /**
     * Returns a full string representation of this  with every field presented.
     * @param decimalPlaces The number of decimals used when presenting floating point values.
     * @return A long-form string representation of this .
     */
    public String toStringFull(int decimalPlaces) {
        return createTable(new Item[]{this}, decimalPlaces);
    }

    /**
     * Returns a clone of this 
     * @return A clone of this 
     */
    @Override
    public Item clone() {
        Item i = new Item(itemNumber, description, amountInStorage, price, category, brand, weight, width, length, color);
        i.setDiscount(this.priceDiscount);
        return i;
    }

    /**
     * Creates a table of all the Items in the submitted array, and returns it as a String.
     * It finds the required width of every column, and formats the result to create straight columns.
     * @param items The items to create a table of
     * @param decimalPlaces The number of digits to use after comma
     * @return The resulting table, or null if the length of parameter 'items' is 0.
     */
    public static String createTable(Item[] items, int decimalPlaces) {
        if (items == null)
            throw new IllegalArgumentException("Parameter 'items' cannot be null");
        if (decimalPlaces < 0)
            throw new IllegalArgumentException("Parameter 'decimalPlaces' must be a positive number");

        if (items.length == 0)
            return null;

        String[][] fields = new String[items.length][];
        fields[0] = items[0].fieldsAsStrings(decimalPlaces); // Perform first iteration outside the loop...
        int numFields = fields[0].length;                    // ... to find the number of fields in a single  ...
        int[] columnLengths = new int[numFields];            // ... and use it to initialize "columnLengths" with the right size.

        for (int item = 0; item < items.length; item++) {             // But we still have to repeat the first iteration...
            fields[item] = items[item].fieldsAsStrings(decimalPlaces);
            for (int field = 0; field < fields[item].length; field++) // ... to execute this loop AFTER we initialize "columnLengths".
                columnLengths[field] = Math.max(columnLengths[field], fields[item][field].length());
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.length - 1; i++)
            sb.append(items[i].toTableString(columnLengths, decimalPlaces)).append("\n");
        sb.append(items[items.length - 1].toTableString(columnLengths, decimalPlaces));

        return sb.toString();
    }

    /**
     * Converts every field in this  to Strings and places them in the returned array. The size of the returned array is equal to .ITEM_NUM_FIELDS.
     * @param decimalPlaces The number of decimal digits used when converting this object's float values to Strings.
     * @return A String array containing the String-representations of this object's fields.
     */
    private String[] fieldsAsStrings(int decimalPlaces) {
        return new String[] {
                itemNumber,
                description,
                String.valueOf(amountInStorage),
                String.valueOf(price),
                String.format("%."+decimalPlaces+"f", priceDiscount),
                category.name(),
                brand,
                String.format("%."+decimalPlaces+"f", weight),
                String.format("%."+decimalPlaces+"f", width),
                String.format("%."+decimalPlaces+"f", length),
                color
        };
    }

    /**
     * Creates a table row containing every field in this .
     * @param columnWidths The width of each column in the table where this  has its own row.
     * @param decimalPlaces The number of decimal digits used when converting this 's float values to Strings.
     * @return A String, containing a table row with every field in this .
     */
    private String toTableString(int[] columnWidths, int decimalPlaces) {
        Formatter sf = new Formatter();
        sf.format("%"    + columnWidths[0]  + "s - ",                             itemNumber);
        sf.format("%-"   + columnWidths[1]  + "s : [",                            description);
        sf.format("%"    + columnWidths[2]  + "d units| ",                        amountInStorage);
        sf.format("%"    + columnWidths[3]  + "d kr| ",                           price);
        sf.format("%"    + columnWidths[4]  + "." + decimalPlaces + "f %% off| ", priceDiscount);
        sf.format("%"    + columnWidths[5]  + "s| ",                              category);
        sf.format("%"    + columnWidths[6]  + "s| ",                              brand);
        sf.format("%"    + columnWidths[7]  + "." + decimalPlaces + "f kg| ",     weight);
        sf.format("w=%"  + columnWidths[8]  + "." + decimalPlaces + "fm| ",       width);
        sf.format("l=%"  + columnWidths[9]  + "." + decimalPlaces + "fm| ",       length);
        sf.format("%"    + columnWidths[10] + "s]",                               color);
        return sf.toString();
    }

//  ### SETTERS ###

    /**
     * Sets the item number of this . Throws an <code>IllegalArgumentException</code> if the submitted parameter is <code>null</code> or blank (as defined by <code>stringObject.isBlank()</code>).
     * @param itemNumber The new item number of this .
     */
    private void setItemNumber(String itemNumber) {
        if (isBlank(itemNumber))
            throw new IllegalArgumentException("The item number of an  cannot be empty");
        this.itemNumber = itemNumber;
    }

    /**
     * Sets the description of this . Throws an <code>IllegalArgumentException</code> if the submitted parameter is <code>null</code> or blank (as defined by <code>stringObject.isBlank()</code>).
     * @param description The new description of this 
     */
    public void setDescription(String description) {
        if (isBlank(description))
            throw new IllegalArgumentException("The description of an  cannot be empty");
        this.description = description;
    }

    /**
     * Sets the amount of this item currently in storage. Throws an <code>IllegalArgumentException</code> if the submitted parameter is a non-positive number.
     * @param amountInStorage The new amount of this item currently in storage.
     */
    public void setAmountInStorage(int amountInStorage) {
        if (amountInStorage < 0)
            throw new IllegalArgumentException("The amount of this  in storage must be a positive value");
        this.amountInStorage = amountInStorage;
    }

    /**
     * Sets the price of this . Throws an <code>IllegalArgumentException</code> if the submitted parameter is a non-positive number.
     * @param price The new price of this item.
     */
    public void setPrice(int price) {
        if (price < 0)
            throw new IllegalArgumentException("Price of an  cannot be less than 0");
        this.price = price;
    }

    /**
     * Sets the discount of this . The value submitted here changes the value returned by <code>itemObject.getPriceAfterDiscount()</code>.
     * Throws an <code>IllegalArgumentException</code> if the submitted parameter is a non-positive number, or higher than 100.
     * @param discount The new discount of this item, in percentage. A discount of 0% equates to disabling the discount.
     */
    public void setDiscount(float discount) {
        if (discount < 0 || discount > 100)
            throw new IllegalArgumentException("percentOff must be a positive value between 0 and 100");
        this.priceDiscount = discount;
    }

    /**
     * Sets the brand of this . Throws an <code>IllegalArgumentException</code> if the submitted parameter is <code>null</code> or blank (as defined by <code>stringObject.isBlank()</code>).
     * @param brand The new brand of this 
     */
    private void setBrand(String brand) {
        if (isBlank(brand))
            throw new IllegalArgumentException("The brand of an  cannot be empty");
        this.brand = brand;
    }

    /**
     * Sets the weight of this . Throws an <code>IllegalArgumentException</code> if the submitted parameter is a non-positive number.
     * @param weight The weight of this item, in kilograms.
     */
    private void setWeight(float weight) {
        if (weight < 0)
            throw new IllegalArgumentException("The weight of an  must be a positive number");
        this.weight = weight;
    }

    /**
     * Sets the length of this . Throws an <code>IllegalArgumentException</code> if the submitted parameter is a non-positive number.
     * @param length The length of this item, in meters.
     */
    private void setLength(float length) {
        if (length < 0)
            throw new IllegalArgumentException("The length of an  must be a positive number");
        this.length = length;
    }

    /**
     * Sets the width of this . Throws an <code>IllegalArgumentException</code> if the submitted parameter is a non-positive number.
     * @param width The width of this item, in meters.
     */
    private void setWidth(float width) {
        if (width < 0)
            throw new IllegalArgumentException("The width of an  must be a positive number");
        this.width = width;
    }

    /**
     * Sets the color of this . Throws an <code>IllegalArgumentException</code> if the submitted parameter is <code>null</code> or blank (as defined by <code>stringObject.isBlank()</code>).
     * @param color The new color of this 
     */
    private void setColor(String color) {
        if (isBlank(color))
            throw new IllegalArgumentException("The color of an  cannot be empty");
        this.color = color;
    }

    /**
     * Sets the category of this .
     * @param category The category to set this  in.
     */
    private void setCategory(ItemCategory category) {
        this.category = category;
    }

    /**
     * Returns the same result as <code>stringObject.isBlank()</code>, but additionally makes sure that <code>stringObject != null</code> before it tries to call <code>isBlank()</code> on the String, hence avoiding any <code>NullPointerException</code>.
     * @param s The String to check.
     * @return True if the parameter is null, or if the string is empty or contains only white space codepoints. Otherwise, it returns false.
     */
    private static boolean isBlank(String s) {
        if (s == null)
            return true;

        return s.isBlank();
    }

//  ### GETTERS ###

    /**
     * Returns the item number of this .
     * @return The item number of this .
     */
    public String getItemNumber() {
        return itemNumber;
    }

    /**
     * Returns the description of this .
     * @return The description of this .
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the price of this . The returned value is NOT adjusted by the discount of this . To get the price adjusted by discount, use <code>itemObject.getPriceAfterDiscount()</code> instead.
     * @return The price of this , NOT adjusted by discount.
     */
    public int getPrice() { return price; }

    /**
     * Returns the price of this  adjusted by the discount (set by a call to <code>itemObject.setDiscount(float discount)</code>).
     * @return The price of this  adjusted for discount.
     */
    public int getPriceAfterDiscount() {
        return (int)(price * (1f - priceDiscount / 100f));
    }

    /**
     * Returns the price discount of this .
     * @return The price discount of this .
     */
    public float getPriceDiscount() {
        return priceDiscount;
    }

    /**
     * Returns the brand this  is associated with.
     * @return The brand this  is associated with.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Returns the weight of this  in kilograms. Does not return the combined weight of all items of this type.
     * @return The weight of this  in kilograms.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Returns the length of this  in metric meters.
     * @return The length of this  in metric meters.
     */
    public float getLength() {
        return length;
    }

    /**
     * Returns the width of this  in metric meters.
     * @return The width of this  in metric meters.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Returns the color of this .
     * @return The color of this .
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the amount of this  currently in storage.
     * @return The amount of this  currently in storage.
     */
    public int getAmountInStorage() {
        return amountInStorage;
    }

    public ItemCategory getCategory() {
        return category;
    }
}
