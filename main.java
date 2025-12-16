import java.lang.Thread.State;
import java.util.*;

class Main {

    public static void main(String[] args) {
        try {
            List<Task> line1Tasks = new ArrayList<>();
            List<Task> line2Tasks = new ArrayList<>();
            List<Task> line3Tasks = new ArrayList<>();
            line1Tasks.add(new Task("Ali", "Phone", 10, "20-12-2025 12:00:00"));
            line1Tasks.add(new Task("Ali", "Phone", 10, "20-12-2025 12:00:00"));
            line1Tasks.add(new Task("Ali", "Phone", 10, "20-12-2025 12:00:00"));
            line2Tasks.add(new Task("Ali", "Phone", 10, "20-12-2025 12:00:00"));
            line2Tasks.add(new Task("Ali", "Phone", 10, "20-12-2025 12:00:00"));
            line2Tasks.add(new Task("Ali", "Phone", 10, "20-12-2025 12:00:00"));
            line3Tasks.add(new Task("Ali", "Phone", 10, "20-12-2025 12:00:00"));
            line3Tasks.add(new Task("Ali", "Phone", 10, "20-12-2025 12:00:00"));
            line3Tasks.add(new Task("Ali", "Phone", 10, "20-12-2025 12:00:00"));
            // إنشاء الخطوط مع حالات صحيحة فقط
            List<ProductLine> lines = new ArrayList<>();
            lines.add(new ProductLine(1, "moha", ProductLine.State.ACTIVE, line1Tasks));
            lines.add(new ProductLine(2, "Line B", ProductLine.State.STOP, line2Tasks));
            lines.add(new ProductLine(3, "Line C", ProductLine.State.MAINTENANCE, line3Tasks));
            // تشغيل الخطوط بالترتيب
            for (ProductLine line : lines) {
                line.start();
                line.join();
            }

            System.out.println("Program finished successfully.");

        } catch (IllegalArgumentException e) {
            // إذا حالة غلط
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted.");
        }
        ProductLine.addLineFromUser();
    }
}