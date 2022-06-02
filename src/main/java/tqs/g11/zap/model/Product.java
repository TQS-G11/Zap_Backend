package tqs.g11.zap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    private int ownerId;

    @Getter
    @Setter
    private Double price;

    public Product(String name, String description, int ownerId){
        this.productName = name;
        this.description = description;
        this.ownerId = ownerId;
        this.price = 0.0;
        this.quantity = 0;
        this.description = "";
    }
}
