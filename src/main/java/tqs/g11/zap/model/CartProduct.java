package tqs.g11.zap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cartProducts")
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @ManyToOne
    private Product product;

    @Getter
    @Setter
    private Integer quantity;

    @Getter
    @Setter
    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return "CartProduct{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                ", user=" + user +
                '}';
    }
}
