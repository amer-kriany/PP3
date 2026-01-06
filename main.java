import java.time.LocalDateTime;
import java.util.*;

class Main {

    public static void main(String[] args) throws InterruptedException {
           // 1️⃣ إنشاء Items
        Item PVP = new Item(1, "PVP", Item.MEDICAL_DEVICES, 5.0, 0, 0);
        Item LAC = new Item(2, "LAC", Item.MEDICAL_DEVICES, 3.0, 0, 0);
        Item Aerosil = new Item(3, "Aerosil", Item.MEDICAL_DEVICES, 4.0, 0, 0);

        // 2️⃣ إضافة Items للـ Inventory
        Inventory.addItem(PVP, 10);
        Inventory.addItem(LAC, 10);
        Inventory.addItem(Aerosil, 10);

        // 3️⃣ تحميل Recipes
        RecipeManager.loadRecipes();

        // 4️⃣ إنشاء خط الإنتاج
        ProductLine line1 = new ProductLine(1, "Line-1", ProductLine.State.ACTIVE);
        line1.start();

        // 5️⃣ إنشاء Task صالح
        Task t1 = new Task("Ahmad", "Tablet", 2, "31-12-2026 18:00:00");
        line1.addTask(t1);

        Thread.sleep(3000);


        // // ⚠️ ملاحظة: ما رح نحمّل Recipes
        // // RecipeManager.loadRecipes(); ← عمداً مش مستدعيها

        // // إنشاء خط إنتاج
        // ProductLine line1 = new ProductLine(
        // 1,
        // "Line-1",
        // ProductLine.State.ACTIVE
        // );

        // line1.start();

        // // Task بمنتج غير موجود بالـ Recipes
        // Task t1 = new Task(
        // "Ahmad",
        // "UnknownProduct", // هذا السبب
        // 5,
        // "31-12-2026 18:00:00"
        // );

        // line1.addTask(t1);

        // // ننتظر شوي حتى ينفذ
        // Thread.sleep(3000);

        // // نوقف الخط
        // line1.setState(ProductLine.State.STOP);

        // // ================== 1️⃣ إنشاء العناصر ==================
        // Item cpu = new Item(1, "CPU", Item.MEDICAL_DEVICES, 120.0, 100, 10);
        // Item ram = new Item(2, "RAM", Item.MEDICAL_DEVICES, 50.0, 100, 10);
        // Item ssd = new Item(3, "SSD", Item.MEDICAL_DEVICES, 80.0, 50, 5);
        // Item screen = new Item(4, "Screen", Item.MEDICAL_DEVICES, 100.0, 50, 5);
        // Item sugar = new Item(5, "Sugar", Item.MEDICINE, 10.0, 20, 5);

        // Inventory.addItem(cpu, 100);
        // Inventory.addItem(ram, 100);
        // Inventory.addItem(ssd, 50);
        // Inventory.addItem(screen, 50);
        // Inventory.addItem(sugar, 20);

        // // ================== 2️⃣ تحميل Recipes ==================
        // RecipeManager.loadRecipes(); // Recipes تعتمد على Items الموجودة في Inventory

        // // ================== 3️⃣ إنشاء خطوط الإنتاج ==================
        // ProductLine line1 = new ProductLine(1, "Line-A", ProductLine.State.ACTIVE);
        // ProductLine line2 = new ProductLine(2, "Line-B", ProductLine.State.ACTIVE);

        // ArrayList<ProductLine> lines = new ArrayList<>();
        // lines.add(line1);
        // lines.add(line2);

        // // ================== 4️⃣ ProductManager ==================
        // ProductionManager manager = new ProductionManager(lines);

        // // ================== 5️⃣ تشغيل خطوط الإنتاج ==================
        // line1.start();
        // line2.start();

        // // ================== 6️⃣ إنشاء وإضافة Tasks ==================
        // Task t1 = new Task("Ahmad", "Laptop", 5, "31-12-2026 18:00:00");
        // line1.addTask(t1);

        // Task t2 = new Task("Sara", "Phone", 10, "31-12-2026 20:00:00");
        // line2.addTask(t2);

        // Task t3 = new Task("Omar", "Laptop", 7, "31-12-2026 22:00:00");
        // line1.addTask(t3);

        // Task t4 = new Task("Lina", "Tablet", 4, "31-12-2026 23:00:00");
        // line2.addTask(t4);

        // // ================== 7️⃣ إعطاء وقت للتنفيذ ==================
        // try {
        // Thread.sleep(5000); // محاكاة تنفيذ التاسكات
        // } catch (InterruptedException e) {
        // System.err.println(e.getMessage());
        // }

        // // ================== 8️⃣ تقرير المنتج الأكثر طلبًا ==================
        // LocalDateTime from = LocalDateTime.now().minusDays(1);
        // LocalDateTime to = LocalDateTime.now().plusDays(1);

        // String mostRequested = manager.getMostRequestedProduct(from, to);

        // System.out.println("\n===============================");
        // System.out.println("Most Requested Product");
        // System.out.println("===============================");
        // System.out.println(mostRequested);

        // // ================== 9️⃣ إيقاف خطوط الإنتاج ==================
        // line1.setState(ProductLine.State.STOP);
        // line2.setState(ProductLine.State.STOP);

        // // ================== إنشاء خطوط الإنتاج ==================
        // ProductLine line1 =
        // new ProductLine(1, "Line-A", ProductLine.State.ACTIVE);

        // ProductLine line2 =
        // new ProductLine(2, "Line-B", ProductLine.State.ACTIVE);

        // // ================== تجميع الخطوط ==================
        // ArrayList<ProductLine> lines = new ArrayList<>();
        // lines.add(line1);
        // lines.add(line2);

        // // ================== ProductManager ==================
        // ProductionManager manager = new ProductionManager(lines);

        // // ================== تشغيل خطوط الإنتاج ==================
        // line1.start();
        // line2.start();

        // // ================== إنشاء وإضافة Tasks ==================
        // Task t1 = new Task(
        // "Ahmad",
        // "Laptop",
        // 5,
        // "31-12-2026 18:00:00"
        // );
        // line1.addTask(t1);

        // Task t2 = new Task(
        // "Sara",
        // "Phone",
        // 10,
        // "31-12-2026 20:00:00"
        // );
        // line2.addTask(t2);

        // Task t3 = new Task(
        // "Omar",
        // "Laptop",
        // 7,
        // "31-12-2026 22:00:00"
        // );
        // line1.addTask(t3);

        // Task t4 = new Task(
        // "Lina",
        // "Tablet",
        // 4,
        // "31-12-2026 23:00:00"
        // );
        // line2.addTask(t4);

        // // نعطي وقت للتنفيذ
        // try {
        // Thread.sleep(5000);
        // } catch (InterruptedException e) {
        // System.err.println(e.getMessage());
        // }

        // // ================== تقرير ==================
        // LocalDateTime from = LocalDateTime.now().minusDays(1);
        // LocalDateTime to = LocalDateTime.now().plusDays(1);

        // String result =
        // manager.getMostRequestedProduct(from, to);

        // System.out.println("\n===============================");
        // System.out.println("Most Requested Product");
        // System.out.println("===============================");
        // System.out.println(result);

        // // ================== إيقاف خطوط الإنتاج ==================
        // line1.setState(ProductLine.State.STOP);
        // line2.setState(ProductLine.State.STOP);
        // }
        // ProductionManager pm = new ProductionManager(new ArrayList<>());

        // ProductLine lineA = new ProductLine(1, "Line A", ProductLine.State.ACTIVE);
        // ProductLine lineB = new ProductLine(2, "Line B", ProductLine.State.ACTIVE);

        // pm.addLine(lineA);
        // pm.addLine(lineB);

        // lineA.addTask(new Task("Ali", "Tablet", 10, "20-12-2026 12:00:00"));
        // lineA.addTask(new Task("Sara", "Tablet", 5, "21-12-2026 12:00:00"));
        // lineB.addTask(new Task("Omar", "Syrub", 7, "22-12-2026 12:00:00"));
        // ProductionManager p = new ProductionManager(new ArrayList<>());
        // LocalDateTime from = LocalDateTime.of(2025, 1, 1, 0, 0);
        // LocalDateTime to = LocalDateTime.of(2027, 1, 31, 23, 59);

        // String result = p.getMostRequestedProduct(from, to);
        // System.out.println(result);

        // lineA.start();
        // lineB.start();

        // // أو تكمل المهمات يدوياً إذا تريد الاختبار بسرعة
        // for (Task task : lineA.getProductLineTasks()) {
        // task.start();
        // task.complete();
        // }
        // for (Task task : lineB.getProductLineTasks()) {
        // task.start();
        // task.complete();
        // }

        // // هون التشغيل
        // pm.showAllManufacturedProducts();

        // try {
        // ArrayList<ProductLine> lines = new ArrayList<>();

        // lines.add(new ProductLine(
        // 1, "moha", ProductLine.State.ACTIVE));

        // ProductionManager manager = new ProductionManager(lines);

        // manager.addTask(
        // new Task("Ali", "Tablet", 10, "20-12-2026 12:00:00"),
        // "moha");

        // manager.addTask(
        // new Task("Sara", "Tablet", 5, "22-12-2026 12:00:00"),
        // "moha");

        // manager.showLinesForSelectedTasksStrict(
        // "Tablet", Arrays.asList(1, 2));
        // manager.showProductsByLine("moha");
        // } catch (Exception e) {
        // System.out.println(e.getMessage());
        // }
        // try {
        // List<Task> line1Tasks = new ArrayList<>();
        // List<Task> line2Tasks = new ArrayList<>();
        // List<Task> line3Tasks = new ArrayList<>();
        // for (int i = 0; i < 3; i++) {
        // line1Tasks.add(new Task("Ali", "Tablet", 10, "20-12-2025 12:00:00"));
        // line2Tasks.add(new Task("Ali", "Tablet", 10, "20-12-2025 12:00:00"));
        // line3Tasks.add(new Task("Ali", "Tablet", 10, "20-12-2025 12:00:00"));
        // }

        // List<ProductLine> lines = new ArrayList<>();
        // lines.add(new ProductLine(1, "moha", ProductLine.State.ACTIVE, line1Tasks));
        // lines.add(new ProductLine(2, "Line B", ProductLine.State.STOP, line2Tasks));
        // lines.add(new ProductLine(3, "Line C", ProductLine.State.MAINTENANCE,
        // line3Tasks));

        // Inventory.addItem(new Item(0, "PVP", "jj", 1, 0, 0), 0);
        // Inventory.addItem(new Item(0, "LAC", "kkk", 2, 0, 0), 0);
        // Inventory.addItem(new Item(0, "Aerosil", "ssl", 3, 0, 0), 0);
        // Inventory.addItem(new Item(0, "Sugar", "slsl", 4, 0, 0), 0);

        // RecipeManager.loadRecipes();

        // Recipe tabletRecipe = RecipeManager.getRecipe("Tablet");
        // System.out.println(tabletRecipe);

        // for (ProductLine line : lines) {
        // line.start();
        // }

        // for (ProductLine line : lines) {
        // line.join();
        // }

        // System.out.println("Program finished successfully.");

        // } catch (IllegalArgumentException e) {
        // System.out.println("Error: " + e.getMessage());
        // } catch (InterruptedException e) {
        // System.out.println("Thread interrupted.");
        // }
    }
}