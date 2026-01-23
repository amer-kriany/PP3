import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // production manager
        ProductionManager pm = new ProductionManager(new ArrayList<>());
        // Item
        Item cpu = new Item(1, "CPU", Item.Categories.TECHNOLOGY, 120.0, 100, 10);
        Item ram = new Item(2, "RAM8", Item.Categories.TECHNOLOGY, 50.0, 100, 10);
        Item ssd = new Item(3, "SSD128", Item.Categories.TECHNOLOGY, 80.0, 100, 5);
        Item screen = new Item(4, "Screen", Item.Categories.TECHNOLOGY, 100.0, 50, 5);
        Item cotton = new Item(5, "cotton", Item.Categories.CLOTHES, 90.0, 50, 5);
        Item polyster = new Item(7, "polyster", Item.Categories.CLOTHES, 90.0, 50, 5);
        Item wool = new Item(8, "wool", Item.Categories.CLOTHES, 90.0, 50, 5);
        Item chicken = new Item(8, "chicken", Item.Categories.CANNED_FOOD, 70.0, 50, 5);
        Item tona = new Item(9, "Fishtuna", Item.Categories.CANNED_FOOD, 100.0, 50, 5);
        Item sardines = new Item(10, "Fishsardine", Item.Categories.CANNED_FOOD, 60.0, 50, 5);
        Item cans = new Item(10, "cans", Item.Categories.CANNED_FOOD, 10.0, 100, 5);
        // Add Item to Inventory
        Inventory.addItem(cpu, 100);
        Inventory.addItem(ram, 100);
        Inventory.addItem(ssd, 50);
        Inventory.addItem(screen, 50);
        Inventory.addItem(cotton, 50);
        Inventory.addItem(polyster, 60);
        Inventory.addItem(wool, 30);
        Inventory.addItem(tona, 30);
        Inventory.addItem(sardines, 20);
        Inventory.addItem(chicken, 80);
        Inventory.addItem(cans, 100);
        // Product Line
        ProductLine lineA = new ProductLine(1, "Technology line", ProductLine.State.ACTIVE);
        pm.addLine(lineA);
        ProductLine lineB = new ProductLine(2, "clothes line", ProductLine.State.ACTIVE);
        pm.addLine(lineB);
        ProductLine lineC = new ProductLine(3, "food line", ProductLine.State.ACTIVE);
        pm.addLine(lineC);
        // Tasks
        Task Laptop = new Task("add 5 laptop", "Client 1", "Laptop", 5, "26-01-2026 20:00:00");
        Task Phone = new Task("add 8 Phone", "Client 2", "Phone", 5, "26-01-2026 20:00:00");
        Task tablet = new Task("make 5 tablet", "Client 1", "tablet", 5, "26-01-2026 20:00:00");
        Task HOODIE = new Task("make 5 HOODIE", "Client 4", "HOODIE", 5, "26-01-2026 20:00:00");
        Task JEANSE = new Task("make 5 JEANSE", "Client 4", "JEANSE", 5, "26-01-2026 20:00:00");
        Task JACKET = new Task("make 5 JACKET", "Client 4", "JACKET", 5, "26-01-2026 20:00:00");
        Task Tona = new Task("make 5 TONA", "Client 5", "tuna", 7, "26-01-2026 20:00:00");
        Task Sardines = new Task("make 5 sardines", "Client 6", "sardines", 7, "26-01-2026 20:00:00");
        Task Lanchun = new Task("make 5 Lanchun", "Client 6", "Lanchun", 7, "26-01-2026 20:00:00");
        // add to production manager
        pm.addTask(Laptop, "Technology line");
        pm.addTask(Phone, "Technology line");
        pm.addTask(tablet, "Technology line");
        pm.addTask(HOODIE, "clothes line");
        pm.addTask(JEANSE, "clothes line");
        pm.addTask(JACKET, "clothes line");
        pm.addTask(Tona, "food line");
        pm.addTask(Sardines, "food line");
        pm.addTask(Lanchun, "food line");
        // run tasks
        lineA.start();
        lineB.start();
        lineC.start();
        // try {
        //     UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // new LoginUI().setVisible(true);
       new ManagerUI(pm).setVisible(true);
     
    }
}
