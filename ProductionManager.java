import java.util.ArrayList;
public class ProductionManager {
    private ArrayList<ProductLine> productLines;
    public ProductionManager(ArrayList<ProductLine> producLines){
        this.productLines=producLines;
    }
    public void addTask(Task task , String lineName){
        ProductLine taskLine= chooseLine(lineName);
        if(taskLine==null)throw new IllegalArgumentException("Product line not found");
        Recipe recipe = RecipeManager.getRecipe(task.getDesireProduct());
        Inventory.consume(recipe, task.getQuantity());
        taskLine.addTask(task);

    }


    public ProductLine chooseLine( String lineName){
        for(ProductLine line: productLines){
            if(lineName.equals(line.getLineName())){
                return line;
            }
        }
        return null;
    }

    
}