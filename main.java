import java.util.*;

class Main {

    public static void main(String[] args) {
        try {
            List<Tasks> line1Tasks = new ArrayList<>();
            List<Tasks> line2Tasks = new ArrayList<>();
            List<Tasks> line3Tasks = new ArrayList<>();
            line1Tasks.add(new Tasks("Ali", "Phone", 10));
            line1Tasks.add(new Tasks("Ali", "Phone", 10));
            line1Tasks.add(new Tasks("Ali", "Phone", 10));
            line2Tasks.add(new Tasks("Ahmad", "Laptop", 5));
            line2Tasks.add(new Tasks("Ahmad", "Laptop", 5));
            line2Tasks.add(new Tasks("Ahmad", "Laptop", 5));
            line3Tasks.add(new Tasks("Mohammad", "Tablet", 7));
            line3Tasks.add(new Tasks("Mohammad", "Tablet", 7));
            line3Tasks.add(new Tasks("Mohammad", "Tablet", 7));
            // إنشاء الخطوط مع حالات صحيحة فقط
            List<ProductLine> lines = new ArrayList<>();
            lines.add(new ProductLine(1, "Line A", "active", line1Tasks));
            lines.add(new ProductLine(2, "Line B", "stop", line2Tasks));
            lines.add(new ProductLine(3, "Line C", "maintenance", line3Tasks));
            // ترتيب حسب lineId
            lines.sort(Comparator.comparingInt(l -> l.lineId));

            // تشغيل الخطوط بالترتيب
            for (ProductLine line : lines) {
                line.start();
                line.join(); // يضمن الترتيب
            }

            System.out.println("Program finished successfully.");

        } catch (IllegalArgumentException e) {
            // إذا حالة غلط
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted.");
        }

    }
}