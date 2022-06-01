package tqs.g11.zap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String name;

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

    @Getter
    @Setter
    private String category;

    @Getter
    @Setter
    @OneToMany(mappedBy = "product")
    private List<CartProduct> carts;

    @Override
    public String toString() {
        return "Product [description=" + description + ", id=" + id + ", img=" + img + ", name=" + name + ", owner="
                + owner.getUsername() + ", price=" + price + ", quantity=" + quantity + ", category=" + category + "]";
    }
}
