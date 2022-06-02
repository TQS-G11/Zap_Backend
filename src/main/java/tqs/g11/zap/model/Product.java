package tqs.g11.zap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "app_products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long productId;

    @Getter
    @Setter
    private String productName;

    @Getter
    @Setter
    private String img;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private Integer quantity;

    @Getter
    @Setter
    @ManyToOne
    private User owner;

    @Getter
    @Setter
    private Double price;

    public Product(String name, String description, User owner){
        this.productName = name;
        this.description = description;
        this.owner = owner;
        this.price = 0.0;
        this.quantity = 0;
        this.description = "";
    }

    @Getter
    @Setter
    private String category;

//    @Getter
//    @Setter
//    @OneToMany(mappedBy = "product")
//    private List<CartProduct> carts;

    @Override
    public String toString() {
        return "Product [description=" + description + ", id=" + productId + ", img=" + img + ", name=" + productName + ", owner="
                + owner.getUsername() + ", price=" + price + ", quantity=" + quantity + ", category=" + category + "]";
    }
}
