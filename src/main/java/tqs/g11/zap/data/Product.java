package tqs.g11.zap.data;

public class Product {
    
    private int id;
    private String name;
    private String img;
    private String description;
    private int quantity;
    private int owner;
    private double price;
    
    public Product(int id, String name, String img, String description, int quantity, int owner, double price) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.description = description;
        this.quantity = quantity;
        this.owner = owner;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product [description=" + description + ", id=" + id + ", img=" + img + ", name=" + name + ", owner="
                + owner + ", price=" + price + ", quantity=" + quantity + "]";
    }
}
