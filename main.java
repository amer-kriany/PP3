import java.util.*;

class Main {

    public static void main(String[] args) {
        try {
            List<Task> line1Tasks = new ArrayList<>();
            List<Task> line2Tasks = new ArrayList<>();
            List<Task> line3Tasks = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                line1Tasks.add(new Task("Ali", "Tablet", 10, "20-12-2025 12:00:00"));
                line2Tasks.add(new Task("Ali", "Tablet", 10, "20-12-2025 12:00:00"));
                line3Tasks.add(new Task("Ali", "Tablet", 10, "20-12-2025 12:00:00"));
            }

            List<ProductLine> lines = new ArrayList<>();
            lines.add(new ProductLine(1, "moha", ProductLine.State.ACTIVE, line1Tasks));
            lines.add(new ProductLine(2, "Line B", ProductLine.State.STOP, line2Tasks));
            lines.add(new ProductLine(3, "Line C", ProductLine.State.MAINTENANCE, line3Tasks));

            Inventory.addItem(new Item(0, "PVP", "jj", 1, 0, 0), 0);
            Inventory.addItem(new Item(0, "LAC", "kkk", 2, 0, 0), 0);
            Inventory.addItem(new Item(0, "Aerosil", "ssl", 3, 0, 0), 0);
            Inventory.addItem(new Item(0, "Sugar", "slsl", 4, 0, 0), 0);

            RecipeManager.loadRecipes();

            Recipe tabletRecipe = RecipeManager.getRecipe("Tablet");
            System.out.println(tabletRecipe);

            for (ProductLine line : lines) {
                line.start();
            }

            for (ProductLine line : lines) {
                line.join();
            }

            System.out.println("Program finished successfully.");

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
        }
    }
}