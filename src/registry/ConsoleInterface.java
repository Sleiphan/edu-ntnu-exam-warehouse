package registry;

import java.util.Scanner;

public class ConsoleInterface {
    /**
     * A term for the user to enter when they wish to cancel an operation where they are supposed to enter a text.
     */
    private static final String CANCEL_TERM = "-";
    private final ItemRegistry registry;
    private final Scanner scanner = new Scanner(System.in);
    private boolean exit = false;

    public ConsoleInterface(ItemRegistry registry) {
        this.registry = registry;
    }

    public void run() {
        while (!exit) {
            open();
        }
    }

    public void open() {
        System.out.println("What do you want to do?");
        System.out.println("1: Print all registered items");
        System.out.println("2: Register new item");
        System.out.println("3: Delete item entry");
        System.out.println("4: Increase the amount of an item");
        System.out.println("5: Decrease the amount of an item");
        System.out.println("6: Select an item to edit");
        System.out.println("0: Exit");

        Integer ans = getAnsAsInt();
        if (ans == null)
            return;

        switch (ans) {
            case 1 -> printAll();
            case 2 -> dialogRegisterNewItem();
            case 3 -> dialogDeleteEntry(selectItem());
            case 4 -> dialogIncreaseAmountInStorage(selectItem());
            case 5 -> dialogDecreaseAmountInStorage(selectItem());
            case 6 -> editItem();
            case 0 -> exit = true;
            default -> System.out.println("Invalid input");
        }
    }

    private void printAll() {
        registry.printAllEntries();
        System.out.println();
    }

    /**
     * Performs a "Register new item" dialog with the user, letting them enter values for all 10 fields of the new item.
     * The user can immediately cancel the process at any time by following the instructions given.
     */
    private void dialogRegisterNewItem() {
        String itemNumber = dialogEnterItemNumber();
        if (itemNumber == null)
            return;
        String description = dialogEnterAString("Enter a description for this item");
        if (description == null)
            return;
        Integer amountInStorage = dialogEnterAnInt("Enter the amount of this item currently in storage");
        if (amountInStorage == null)
            return;
        Integer price = dialogEnterAnInt("Enter a price for this item");
        if (price == null)
            return;
        ItemCategory category = dialogEnterItemCategory();
        if (category == null)
            return;
        String brand = dialogEnterAString("Enter the brand associated with this item");
        if (brand == null)
            return;
        Float weight = dialogEnterAFloat("Enter the weight of this item");
        if (weight == null)
            return;
        Float width = dialogEnterAFloat("Enter the width of this item");
        if (width == null)
            return;
        Float length = dialogEnterAFloat("Enter the length of this item");
        if (length == null)
            return;
        String color = dialogEnterAString("Enter the color of this item");
        if (color == null)
            return;

        registry.registerNewItem(itemNumber, description, amountInStorage, price, category, brand, weight, width, length, color);

    }

    /**
     * Performs a dialog with the user helping them to enter an item number not already taken.
     * @return The item number entered by the user, or null if the user canceled the action.
     */
    private String dialogEnterItemNumber() {
        while (true) {
            String itemNumber = dialogEnterAString("Enter an item number for this item");
            if (itemNumber == null)
                return null;
            if (itemNumber.equals(CANCEL_TERM))
                return null;

            if (registry.itemNumberTaken(itemNumber)) {
                System.out.println("Item number already claimed by another item");
                continue;
            }

            return itemNumber;
        }

    }

    /**
     * Performs a dialog with the user to let them enter an item category.
     * @return The item category entered by the user, or null if the user canceled the action.
     */
    private ItemCategory dialogEnterItemCategory() {
        final ItemCategory[] cat = ItemCategory.values();
        final StringBuilder categoryExplanation = new StringBuilder();
        for (int i = 0; i < cat.length - 1; i++)
            categoryExplanation.append((i + 1)).append("=").append(cat[i]).append(", ");
        categoryExplanation.append(cat.length).append("=").append(cat[cat.length - 1]);

        while (true) {
            Integer i = dialogEnterAnInt("Enter an item category, where " + categoryExplanation.toString());
            if (i == null)
                return null;
            if (i < 1 || i > cat.length) {
                System.out.println("Item category must be a digit between 1 and " + cat.length);
                continue;
            }

            i--;
            return ItemCategory.values()[i];
        }

    }

    private String dialogEnterAString(String query) {
        while (true) {
            System.out.println(query + "\n("+CANCEL_TERM+" to cancel)");
            String s = scanner.nextLine();
            if (s == null || s.isBlank()) {
                System.out.println("Empty expression not accepted");
                continue;
            }
            if (s.equals(CANCEL_TERM))
                return null;

            return s;
        }
    }

    private Float dialogEnterAFloat(String query) {
        while (true) {
            System.out.println(query + "\n(-1 to cancel)");
            Float f = getAnsAsFloat();
            if (f == null)
                continue;
            if (f == -1)
                return null;

            if (f < 0) {
                System.out.println("Only positive values accepted");
                continue;
            }

            return f;
        }
    }

    private Integer dialogEnterAnInt(String query) {
        while (true) {
            System.out.println(query + "\n(-1 to cancel)");
            Integer i = getAnsAsInt();
            if (i == null)
                continue;
            if (i == -1)
                return null;

            if (i < 0) {
                System.out.println("Only positive values accepted");
                continue;
            }

            return i;
        }
    }



    private void dialogIncreaseAmountInStorage(String itemNumber) {
        if (itemNumber == null)
            return;

        int available = registry.getItem(itemNumber).getAmountInStorage();
        Integer ans = dialogEnterAnInt("How many of that item would you like to add to storage? Available: " + available);

        if (ans == null)
            return;

        registry.increaseAmountInStorage(itemNumber, ans);

        int newAmount = registry.getItem(itemNumber).getAmountInStorage();
        System.out.println("Success! New amount stored: " + newAmount);
    }

    private void dialogDecreaseAmountInStorage(String itemNumber) {
        Integer ans = null;

        while (true) {
            if (itemNumber == null)
                return;

            int available = registry.getItem(itemNumber).getAmountInStorage();
            if (available == 0) {
                System.out.println("No more available items of this type.");
                acknowledge();
                return;
            }
            ans = dialogEnterAnInt("How many of that item would you like to withdraw from the storage? Available: " + available);
            if (ans == null)
                return;

            if (ans > available) {
                System.out.println("Cannot withdraw more items than the available amount");
                continue;
            }

            break;
        }

        registry.decreaseAmountInStorage(itemNumber, ans);

        int newAmount = registry.getItem(itemNumber).getAmountInStorage();
        System.out.println("Success! New amount in storage: " + newAmount);
    }

    private boolean dialogDeleteEntry(String itemNumber) {
        if (itemNumber == null)
            return false;

        System.out.println("Are you sure you want to delete this item? [y/n]:\n" + registry.getItem(itemNumber).toStringFull());
        String ans = scanner.nextLine();

        if (ans.equals("y")) {
            System.out.println("Item deleted");
            return registry.deleteItemEntry(itemNumber);
        }
        else {
            System.out.println("Aborted");
            return false;
        }
    }


    /**
     * Enters an "item menu", that relieves them of having to select the same item over and over again.
     * First, the user selects an item. Then, they can perform multiple actions on that item.
     * Lastly, they exit the menu (deselecting the item) and can continue from the main menu again.
     */
    private void editItem() {
        String itemNumber = selectItem();
        if (itemNumber == null)
            return;

        boolean exitItem = false;

        while (!exitItem) {
            System.out.println("\nSelected item: " + registry.getItem(itemNumber).toStringFull());
            System.out.println("What do you want to do with this item?");
            System.out.println("1: Increase its amount in storage");
            System.out.println("2: Decrease its amount in storage");
            System.out.println("3: Edit price");
            System.out.println("4: Edit discount");
            System.out.println("5: Edit description");
            System.out.println("6: Delete this item");
            System.out.println("0: Exit");

            Integer ans = getAnsAsInt();
            if (ans == null)
                continue;

            switch (ans) {
                case 1 -> dialogIncreaseAmountInStorage(itemNumber);
                case 2 -> dialogDecreaseAmountInStorage(itemNumber);
                case 3 -> editPrice(itemNumber);
                case 4 -> editDiscount(itemNumber);
                case 5 -> editDescription(itemNumber);
                case 6 -> {
                    if (dialogDeleteEntry(itemNumber))
                        exitItem = true;
                }
                case 0 -> exitItem = true;
                default -> {
                    System.out.println("Invalid input");
                    return;
                }
            }
        }
    }

    private void editPrice(String itemNumber) {
        Integer ans = dialogEnterAnInt("Enter the new price. Current: " + registry.getItem(itemNumber).getPrice());
        if (ans == null)
            return;

        registry.setItemPrice(itemNumber, ans);
    }

    private void editDiscount(String itemNumber) {
        while (true ) {
            Float ans = dialogEnterAFloat("Enter the new discount (%). Current: " + registry.getItem(itemNumber).getPriceDiscount() + ". \"0\" disables the discount.");
            if (ans == null)
                return;
            if (ans < 0 || ans > 100) {
                System.out.println("Discount has to be a value between 0 and 100.");
                continue;
            }

            registry.setItemDiscount(itemNumber, ans);
            break;
        }
    }

    private void editDescription(String itemNumber) {
        String ans = dialogEnterAString("Enter the new description. Current: " + registry.getItem(itemNumber).getDescription());
        if (ans == null)
            return;

        registry.setItemDescription(itemNumber, ans);
    }



    /**
     * Performs a "Select item" dialog with the user.
     * @return The item number of the selected Item, or null if the user canceled the action.
     */
    private String selectItem() {
        while (true) {
            System.out.println("Select an item by...");
            System.out.println("1: Page turning");
            System.out.println("2: Search");
            System.out.println("0: [Cancel]");

            Integer ans = getAnsAsInt();
            if (ans == null)
                continue;
            if (ans == 0)
                return null;

            String itemNumber = null;
            switch (ans) {
                case 1 -> itemNumber = selectItemByPages(registry.getAll());
                case 2 -> itemNumber = selectItemBySearch();
                default -> {
                    System.out.println("Invalid input");
                    continue;
                }
            }

            if (itemNumber != null)
                return itemNumber;
        }
    }

    /**
     * Performs a dialog with the user, letting them select from a list of items.
     * Also features page-turning functionality, with 8 items per page.
     * @param items The items the user can choose from.
     * @return The item number of the selected Item.
     */
    private String selectItemByPages(Item[] items) {
        int firstIndex = 0;
        final int NUM_ENTRIES_PER_PAGE = 7;
        int pageNumber = 1;
        int numPages = items.length / NUM_ENTRIES_PER_PAGE + 1;
        Integer ans = null;

        while (true) {
            System.out.println("Select an item [Page "+ (firstIndex / NUM_ENTRIES_PER_PAGE + 1) + "]");
            System.out.println("0: Cancel");
            for (int i = 0; i < NUM_ENTRIES_PER_PAGE && i + firstIndex < items.length; i++)
                System.out.println(i + 1 + ": " + items[i + firstIndex]);

            if (firstIndex - NUM_ENTRIES_PER_PAGE >= 0)
                System.out.println("8: [Previous page]");
            if (firstIndex + NUM_ENTRIES_PER_PAGE < items.length)
                System.out.println("9: [Next page]");

            ans = getAnsAsInt();
            if (ans == null)
                continue;
            if (ans == 0)
                return null;
            if (ans > 0 && ans < 8 && ans + firstIndex - 1 < items.length)
                break;

            if (ans == 8 && firstIndex - NUM_ENTRIES_PER_PAGE >= 0) {
                firstIndex -= NUM_ENTRIES_PER_PAGE;
                continue;
            }
            if (ans == 9 && firstIndex + NUM_ENTRIES_PER_PAGE < items.length) {
                firstIndex += NUM_ENTRIES_PER_PAGE;
                continue;
            }

            System.out.println("Invalid input: " + ans);
        }

        ans--;
        return items[ans + firstIndex].getItemNumber();
    }

    /**
     * Performs a dialog with the user to search for an item in the registry, and select among the results.
     * @return The item number of the selected item, or null if the user canceled the action.
     */
    private String selectItemBySearch() {
        boolean searchByDescription = false;

        while (true) {
            System.out.println("Search by...");
            System.out.println("1: Description");
            System.out.println("2: Item number");
            System.out.println("0: Cancel");

            Integer ans = getAnsAsInt();
            if (ans == null)
                continue;
            if (ans == 0)
                return null;

            switch (ans) {
                case 1 -> searchByDescription = true;
                case 2 -> searchByDescription = false;
                default -> {
                    System.out.println("Invalid input");
                    continue;
                }
            }

            while (true) {
                System.out.println("Enter a search term (or "+ CANCEL_TERM +" to cancel):");
                String searchTerm = scanner.nextLine();

                if (searchTerm.equals(CANCEL_TERM))
                    break;

                Item[] results;
                if (searchByDescription)
                    results = registry.getItems(registry.searchByDescription(searchTerm));
                else
                    results = registry.getItems(registry.searchByItemNumber(searchTerm));

                if (results.length > 0) {
                    System.out.println("Found "+results.length+" results.");
                    String selected = selectItemByPages(results);
                    if (selected == null)
                        break;
                    else
                        return selected;
                }

                System.out.println("No results for the term: " + searchTerm);
            }
        }
    }

    /**
     * Helper-function. Reads an integer answer from the user and returns it.
     * If the answer could not be converted to an integer, it prints "Did not understand input" to the console and returns null.
     * @return User input as an integer, or null if the answer could not be converted.
     */
    private Integer getAnsAsInt() {
        String ans = scanner.nextLine();
        try {
            return Integer.parseInt(ans);
        } catch (NumberFormatException e) {
            System.out.println("Did not understand input");
            return null;
        }
    }

    /**
     * Helper-function. Reads a decimal value answer from the user and returns it.
     * If the answer could not be converted to a float, it prints "Did not understand input" to the console and returns null.
     * @return User input as float, or null if the answer could not be converted.
     */
    private Float getAnsAsFloat() {
        String ans = scanner.nextLine();
        try {
            return Float.parseFloat(ans);
        } catch (NumberFormatException e) {
            System.out.println("Did not understand input");
            return null;
        }
    }

    /**
     * Requires the user to press enter (or enter something, depending on the console) to continue.
     * Useful when important messages are delivered to the user, or when printing long lists of strings at once.
     */
    private void acknowledge() {
        System.out.println("Enter to continue.");
        scanner.nextLine();
    }
}
