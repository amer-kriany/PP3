public class Product {
    private int proId;
    private String proName;


    public Product(int proId, String proName) {
        this.proId = proId;
        this.proName = proName;
       
    }

    // ======== Getters ========
    public int getProId() {
        return proId;
    }

    public String getProName() {
        return proName;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Product)) return false;
        Product p = (Product) o;
        return proId == p.proId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(proId);
    }

    @Override
    public String toString() {
        return this.proName; 
    }
}

