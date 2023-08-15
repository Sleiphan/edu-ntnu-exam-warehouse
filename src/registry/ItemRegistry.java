package registry;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemRegistry {
    private final HashMap<String, Item> registry = new HashMap<>();

    public void printAllEntries() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        final int NUM_DECIMALS = 2;

        if (registry.size() != 0)
            return Item.createTable(getAllItemsRef(), NUM_DECIMALS);
        else
            return "[Empty]";
    }

    /**
     * Searches through the registry for any item number that contains the submitted search term.
     * The search performed is NOT case-sensitive.
     * @param searchTerm The term which all returned items has to contain within their item number.
     * @return The search result as an array of item numbers, or null if no relevant items were found.
     */
    public String[] searchByItemNumber(String searchTerm) {
        ArrayList<String> itemNumbers = new ArrayList<>();
        for (String key : registry.keySet().toArray(new String[0]))
            if (key.toLowerCase().contains(searchTerm.toLowerCase()))
                itemNumbers.add(key);

        return itemNumbers.toArray(new String[0]);
    }

    /**
     * Searches through the registry for any Item with a description that contains the submitted search term.
     * The search performed is NOT case-sensitive.
     * @param searchTerm The term which all returned items has to contain within their description.
     * @return The search result as an array of item numbers, or null if no relevant items were found.
     */
    public String[] searchByDescription(String searchTerm) {
        ArrayList<String> itemNumbers = new ArrayList<>();
        for (Item val : registry.values().toArray(new Item[0]))
            if (val.getDescription().toLowerCase().contains(searchTerm.toLowerCase()))
                itemNumbers.add(val.getItemNumber());

        return itemNumbers.toArray(new String[0]);
    }


    /**
     * Registers a new Item to this registry.
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
     * @throws IllegalArgumentException if the submitted item number has already been claimed by another Item.
     */
    public void registerNewItem(String itemNumber, String description, int amountInStorage, int price, ItemCategory category, String brand, float weight, float width, float length, String color) {
        if (itemNumberTaken(itemNumber))
            throw new IllegalArgumentException("The item number " + itemNumber + " is already in use");
        registry.put(itemNumber, new Item(itemNumber, description, amountInStorage, price, category, brand, weight, width, length, color));
    }


    /**
     * Increases the amount of a particular item currently stored in the warehouse.
     * @param itemNumber The item number of the Item to have its amount increased.
     * @param amount The amount of items to be added to the warehouse storage.
     */
    public void increaseAmountInStorage(String itemNumber, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be a positive value");
        changeAmountInStorage(itemNumber, amount);

    }

    /**
     * Decreases the amount of a particular item currently stored in the warehouse.
     * @param itemNumber The item number of the Item to have its amount decreased.
     * @param amount The amount of items to withdraw from the warehouse storage.
     */
    public void decreaseAmountInStorage(String itemNumber, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be a positive value");
        changeAmountInStorage(itemNumber, -amount);
    }

    /**
     * Changes the amount of a particular item currently stored in the warehouse.
     * @param itemNumber The item number of the Item to have its amount increased or decreased.
     * @param amount The amount of items to add (positive values) or withdraw (negative values) from the warehouse storage.
     */
    public void changeAmountInStorage(String itemNumber, int amount) {
        Item i = getItemRef(itemNumber);
        i.setAmountInStorage(i.getAmountInStorage() + amount);
    }



    /**
     * Deletes from the registry the item with the submitted item number.
     * @param itemNumber The item number of the item to be deleted from the registry.
     * @return True if the item was found and deleted. Otherwise, returns false.
     */
    public boolean deleteItemEntry(String itemNumber) {
        return registry.remove(itemNumber) != null;
    }

    /**
     * Sets a new price for a specific Item.
     * @param itemNumber The item number of the Item to set a new price for.
     * @param newPrice The new price of the Item.
     */
    public void setItemPrice(String itemNumber, int newPrice) {
        getItemRef(itemNumber).setPrice(newPrice);
    }

    /**
     * Sets a new price discount for a specific Item.
     * @param itemNumber The item number of the Item to set a new price for.
     * @param percentOff The discount to be assigned to the Item, in percentage between 0 and 100.
     */
    public void setItemDiscount(String itemNumber, float percentOff) {
        getItemRef(itemNumber).setDiscount(percentOff);
    }

    /**
     * Gives a new description to a specific Item.
     * @param itemNumber The item number of the Item to set a new price for.
     * @param description The new description for the Item.
     */
    public void setItemDescription(String itemNumber, String description) {
        getItemRef(itemNumber).setDescription(description);
    }

    /**
     * Fills this registry with some test data.
     */
    public void fillWithTestData() {
        registerNewItem("HA56Y3", "Mahogany door, 240cm", 34, 541, ItemCategory.Doors, "Bendell", 14.35f, 1.02f, 2.40f, "Brown");
        registerNewItem("WE2785", "Small circular window", 58, 784, ItemCategory.Windows, "Dynamik", 3.56f, 0.5f, 0.5f, "Chrome");
        registerNewItem("G15BF8", "Bathroom tiles, 22-pack", 4, 1061, ItemCategory.Floors, "Bendell", 3.94f, 0.098f, 0.198f, "Grey");
        registerNewItem("19FT65", "Mahogany door, 240cm", 96, 1207, ItemCategory.Doors, "Ikea", 16.77f, 1.02f, 2.40f, "Brown");
        registerNewItem("QW05ER", "Lawn chair in birch", 11, 1745, ItemCategory.Wood, "Bendell", 9.9f, 1.12f, 2.0f, "Brown");
        registerNewItem("FJ8I7T", "Designer door carved from willow", 18, 3508, ItemCategory.Doors, "Heidal", 16.7f, 1.02f, 2.0f, "Dark brown");
        registerNewItem("FW312V", "Birch floor planks, 10-pack", 179, 430, ItemCategory.Wood, "Ikea", 7.4f, 0.2f, 1.0f, "Beige");
        registerNewItem("5MN6L9", "Large window, 3x5 meters", 89, 4570, ItemCategory.Windows, "Bendell", 16.7f, 3.16f, 5.16f, "White");
        registerNewItem("P2YI2L", "Epoxy office desk in dark wood", 29, 9899, ItemCategory.Tables, "Heidal", 16.7f, 2.63f, 1.38f, "Dark brown");
        registerNewItem("P2YI2T", "Charger X4 Sofa", 35, 6045, ItemCategory.Chairs, "Bendell", 45.1f, 2.52f, 1.12f, "Black");
        registerNewItem("BW9S24", "Maple planks, 2x4 inches", 526, 56, ItemCategory.Wood, "Dynamik", 4.7f, 0.1016f, 1f, "Brown");
        registerNewItem("BW3S24", "Birch planks, 2x4 inches", 474, 38, ItemCategory.Wood, "Dynamik", 3.1f, 0.1016f, 1f, "Beige");
        registerNewItem("BW4S24", "Oak planks, 2x4 inches", 533, 45, ItemCategory.Wood, "Dynamik", 3.8f, 0.1016f, 1f, "Brown");
    }



    /**
     * Returns a copy of the Item that has the submitted item number. Changes made to the returned Item does not make any changes to this registry.
     * @param itemNumber The item number of the requested Item.
     * @return A copy of the requested Item.
     */
    public Item getItem(String itemNumber) {
        return getItemRef(itemNumber).clone();
    }

    /**
     * Returns the Item in this registry with the given item number, and throws an exception if the given item number does not exist in this registry.
     * @param itemNumber The item number of the requested item.
     * @return The Item with the given item number.
     */
    private Item getItemRef(String itemNumber) {
        Item i = registry.get(itemNumber);
        if (i == null)
            throw new IllegalArgumentException("No items in this registry with this item number: " + itemNumber);
        return i;
    }

    /**
     * Returns an array of Items based on the submitted item numbers. Equivalent of calling 'getItem(...)' multiple times to convert an array of item numbers into Items.
     * Changes to the returned Items does not cause any changes to this registry. Contents are sorted one to one with the contents of 'itemNumbers'.
     * e.g. the Item at index 0 in the returned array, has the item number at index 0 in 'itemNumbers'. Item 1 has item number at index 1, etc.
     * @param itemNumbers The item numbers of the requested Items.
     * @return An array of the requested Items, sorted one to one.
     */
    public Item[] getItems(String[] itemNumbers) {
        Item[] result = new Item[itemNumbers.length];

        for (int i = 0; i < itemNumbers.length; i++)
            result[i] = getItem(itemNumbers[i]);

        return result;
    }

    /**
     * Returns an array of Items based on the submitted item numbers. Equivalent of calling 'getItemRef(...)' multiple times to convert an array of item numbers into Items.
     * Contents are sorted one to one with the contents of 'itemNumbers'.
     * e.g. the Item at index 0 in the returned array, has the item number at index 0 in 'itemNumbers'. Item 1 has item number at index 1, etc.
     * @param itemNumbers The item numbers of the requested Items.
     * @return An array of the requested Items, sorted one to one.
     */
    private Item[] getItemsRef(String[] itemNumbers) {
        Item[] result = new Item[itemNumbers.length];

        for (int i = 0; i < itemNumbers.length; i++)
            result[i] = getItemRef(itemNumbers[i]);

        return result;
    }

    /**
     * Returns an array containing a copy of every Item in this registry.
     * @return An array containing a copy of every Item in this registry.
     */
    public Item[] getAll() {
        Item[] references = getAllItemsRef();
        Item[] result = new Item[references.length];

        for (int i = 0; i < references.length; i++)
            result[i] = references[i].clone();

        return result;
    }

    /**
     * Returns an array of every Item in this registry.
     * @return An array of every Item in this registry.
     */
    private Item[] getAllItemsRef() {
        return registry.values().toArray(new Item[0]);
    }

    /**
     * Checks if the submitted item number is used by another Item in this registry.
     * @param itemNumber The item number to check.
     * @return True of the item number is claimed by another Item, or false if the item number is available.
     */
    public boolean itemNumberTaken(String itemNumber) {
        return registry.containsKey(itemNumber);
    }
}
