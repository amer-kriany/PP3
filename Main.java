import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        ProductionManager pm = new ProductionManager(new ArrayList<>());

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

        Product laptop = new Product(1, "Laptop");
        Product phone = new Product(2, "Phone");
        Product tablet = new Product(3, "Tablet");
        Product hoodie = new Product(4, "HOODIE");
        Product jeanse = new Product(5, "JEANSE");
        Product jacket = new Product(6, "JACKET");
        Product tuna = new Product(7, "tuna");
        Product sardines = new Product(8, "sardines");
        Product lanchunProduct = new Product(9, "Lanchun");

        ProductLine lineA = new ProductLine(1, "Technology line", ProductLine.State.ACTIVE);
        ProductLine lineB = new ProductLine(2, "clothes line", ProductLine.State.ACTIVE);
        ProductLine lineC = new ProductLine(3, "food line", ProductLine.State.ACTIVE);
        pm.addLine(lineA);
        pm.addLine(lineB);
        pm.addLine(lineC);

        Task laptopTask = new Task("make 5 Laptop", "Client 1", laptop, 5, "26-12-2026 20:00:00");
        Task phoneTask = new Task("make 5 Phone", "Client 2", phone, 5, "26-12-2026 20:00:00");
        Task tabletTask = new Task("make 5 Tablet", "Client 3", tablet, 5, "26-12-2026 20:00:00");
        Task hoodieTask = new Task("make 5 HOODIE", "Client 4", hoodie, 5, "26-12-2026 20:00:00");
        Task jeanseTask = new Task("make 5 JEANSE", "Client 4", jeanse, 5, "26-12-2026 20:00:00");
        Task jacketTask = new Task("make 5 JACKET", "Client 4", jacket, 5, "26-12-2026 20:00:00");
        Task tunaTask = new Task("make 8 Tuna", "Client 5", tuna, 8, "26-12-2026 20:00:00");
        Task sardinesTask = new Task("make 7 Sardines", "Client 6", sardines, 7, "26-12-2026 20:00:00");
        Task lanchunTask = new Task("make 7 Lanchun", "Client 6", lanchunProduct, 7, "26-12-2026 20:00:00");

        pm.addTask(laptopTask, "Technology line");
        pm.addTask(phoneTask, "Technology line");
        pm.addTask(tabletTask, "Technology line");
        pm.addTask(hoodieTask, "clothes line");
        pm.addTask(jeanseTask, "clothes line");
        pm.addTask(jacketTask, "clothes line");
        pm.addTask(tunaTask, "food line");
        pm.addTask(sardinesTask, "food line");
        pm.addTask(lanchunTask, "food line");

        lineA.start();
        lineB.start();
        lineC.start();

        SwingUtilities.invokeLater(() -> new LoginUI(pm).setVisible(true));
    }
}
