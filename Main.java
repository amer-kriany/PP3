import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // ===================== Production Manager
        ProductionManager pm = new ProductionManager(new ArrayList<>());

        // ===================== Items (Inventory)
        Item cpu = new Item(1, "CPU", Item.Categories.TECHNOLOGY, 120.0, 100, 10);
        Item ram = new Item(2, "RAM8", Item.Categories.TECHNOLOGY, 50.0, 150, 10);
        Item ssd = new Item(3, "SSD128", Item.Categories.TECHNOLOGY, 80.0, 150, 5);
        Item screen = new Item(4, "Screen", Item.Categories.TECHNOLOGY, 100.0, 100, 5);
        Item cotton = new Item(5, "cotton", Item.Categories.CLOTHES, 90.0, 100, 5);
        Item polyster = new Item(6, "polyster", Item.Categories.CLOTHES, 90.0, 100, 5);
        Item wool = new Item(7, "wool", Item.Categories.CLOTHES, 90.0, 100, 5);
        Item chicken = new Item(8, "chicken", Item.Categories.CANNED_FOOD, 70.0, 100, 5);
        Item fishtuna = new Item(9, "Fishtuna", Item.Categories.CANNED_FOOD, 100.0, 100, 5);
        Item fishSardines = new Item(10, "Fishsardine", Item.Categories.CANNED_FOOD, 60.0, 100, 5);
        Item cans = new Item(11, "cans", Item.Categories.CANNED_FOOD, 10.0, 100, 5);

        // Add Items to Inventory
        Inventory.addItem(cpu, 100);
        Inventory.addItem(ram, 150);
        Inventory.addItem(ssd, 150);
        Inventory.addItem(screen, 100);
        Inventory.addItem(cotton, 100);
        Inventory.addItem(polyster, 100);
        Inventory.addItem(wool, 100);
        Inventory.addItem(chicken, 100);
        Inventory.addItem(fishtuna, 100);
        Inventory.addItem(fishSardines, 100);
        Inventory.addItem(cans, 100);

        // ===================== Products
        Product Laptop = new Product(1, "Laptop");

        Product Phone = new Product(2, "Phone");

        Product Tablet = new Product(3, "Tablet");

        Product HOODIE = new Product(4, "HOODIE");

        Product JEANSE = new Product(5, "JEANSE");

        Product JACKET = new Product(6, "JACKET");

        Product Tuna = new Product(7, "tuna");
        Product Sardines = new Product(8, "sardines");

        Product LanchunProduct = new Product(9, "Lanchun");

        // ===================== Product Lines
        ProductLine lineA = new ProductLine(1, "Technology line", ProductLine.State.ACTIVE);
        pm.addLine(lineA);
        ProductLine lineB = new ProductLine(2, "clothes line", ProductLine.State.ACTIVE);
        pm.addLine(lineB);
        ProductLine lineC = new ProductLine(3, "food line", ProductLine.State.ACTIVE);
        pm.addLine(lineC);

        // ===================== Tasks
        Task laptopTask = new Task("make 5 Laptop", "Client 1", Laptop, 5, "26-01-2026 20:00:00");
        Task phoneTask = new Task("make 5 Phone", "Client 2", Phone, 5, "26-01-2026 20:00:00");
        Task tabletTask = new Task("make 5 Tablet", "Client 3", Tablet, 5, "26-01-2026 20:00:00");
        Task hoodieTask = new Task("make 5 HOODIE", "Client 4", HOODIE, 5, "26-01-2026 20:00:00");
        Task jeanseTask = new Task("make 5 JEANSE", "Client 4", JEANSE, 5, "26-01-2026 20:00:00");
        Task jacketTask = new Task("make 5 JACKET", "Client 4", JACKET, 5, "26-01-2026 20:00:00");
        Task tunaTask = new Task("make 5 Tuna", "Client 5", Tuna, 7, "26-01-2026 20:00:00");
        Task sardinesTask = new Task("make 5 Sardines", "Client 6", Sardines, 7, "26-01-2026 20:00:00");
        Task lanchunTask = new Task("make 5 Lanchun", "Client 6", LanchunProduct, 7, "26-01-2026 20:00:00");

        // add Tasks to Production Manager
        pm.addTask(laptopTask, "Technology line");
        pm.addTask(phoneTask, "Technology line");
        pm.addTask(tabletTask, "Technology line");
        pm.addTask(hoodieTask, "clothes line");
        pm.addTask(jeanseTask, "clothes line");
        pm.addTask(jacketTask, "clothes line");
        pm.addTask(tunaTask, "food line");
        pm.addTask(sardinesTask, "food line");
        pm.addTask(lanchunTask, "food line");
        // ===================== Start Production Lines
        lineA.start();
        lineB.start();
        lineC.start();

        // ===================== Launch GUI
        SwingUtilities.invokeLater(() -> new LoginUI(pm).setVisible(true));
    }
}
