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
        Item tuna = new Item(9, "tuna", Item.Categories.CANNED_FOOD, 100.0, 100, 5);
        Item sardines = new Item(10, "sardines", Item.Categories.CANNED_FOOD, 60.0, 100, 5);
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
        Inventory.addItem(tuna, 100);
        Inventory.addItem(sardines, 100);
        Inventory.addItem(cans, 100);

        // ===================== Products
        Product Laptop = new Product(1, "Laptop");
        Laptop.addFormerByName("CPU", 1);
        Laptop.addFormerByName("RAM8", 2);
        Laptop.addFormerByName("SSD128", 1);
        Laptop.addFormerByName("Screen", 1);

        Product Phone = new Product(2, "Phone");
        Phone.addFormerByName("CPU", 1);
        Phone.addFormerByName("RAM8", 1);
        Phone.addFormerByName("Screen", 1);

        Product Tablet = new Product(3, "Tablet");
        Tablet.addFormerByName("CPU", 1);
        Tablet.addFormerByName("RAM8", 2);
        Tablet.addFormerByName("Screen", 1);

        Product HOODIE = new Product(4, "HOODIE");
        HOODIE.addFormerByName("cotton", 3);
        Product JEANSE = new Product(5, "JEANSE");
        JEANSE.addFormerByName("polyster", 3);
        Product JACKET = new Product(6, "JACKET");
        JACKET.addFormerByName("wool", 4);

        Product TunaProduct = new Product(7, "tuna");
        TunaProduct.addFormerByName("tuna", 2);
        Product SardinesProduct = new Product(8, "sardines");
        SardinesProduct.addFormerByName("sardines", 3);
        Product LanchunProduct = new Product(9, "Lanchun");
        LanchunProduct.addFormerByName("cans", 3);
        LanchunProduct.addFormerByName("chicken", 2);

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
        Task tunaTask = new Task("make 5 TUNA", "Client 5", TunaProduct, 7, "26-01-2026 20:00:00");
        Task sardinesTask = new Task("make 5 Sardines", "Client 6", SardinesProduct, 7, "26-01-2026 20:00:00");
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
