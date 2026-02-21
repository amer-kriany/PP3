import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileManager {

    private static final String INVENTORY_FILE = "inventory.txt";
    private static final String ERROR_FILE = "error.txt";

    public static void loadInventory() {
        File file = new File(INVENTORY_FILE);
        if (!file.exists()) {
            System.out.println("Inventory file not found. Starting with empty inventory.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    Item.Categories category = Item.Categories.valueOf(parts[2].trim());
                    double price = Double.parseDouble(parts[3].trim());
                    int quantity = Integer.parseInt(parts[4].trim());
                    int minQuantity = Integer.parseInt(parts[5].trim());

                    Item item = new Item(id, name, category, price, quantity, minQuantity);
                    Inventory.addItem(item, quantity);
                }
            }
        } catch (IOException  | IllegalArgumentException e) {
            logError("Error loading inventory: " + e.getMessage());
        }
    }

    public static void saveInventory(Inventory inventory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {
            for (Item item : Inventory.getStock().keySet()) {
                // Creating a line of text (CSV) using Getters
                String line = item.getId() + "," +
                        item.getName() + "," +
                        item.getCategory() + "," +
                        item.getPrice() + "," +
                        item.getQuantity() + "," +
                        item.getMinQuantity();

                writer.write(line);
                writer.newLine();
            }
            System.out.println("Inventory saved successfully to " + INVENTORY_FILE);
        } catch (IOException e) {
            logError("Error saving inventory: " + e.getMessage());
        }
    }

    public static void logError(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ERROR_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write to error log: " + e.getMessage());
        }
    }
}
