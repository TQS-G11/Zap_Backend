package tqs.g11.zap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.g11.zap.model.CartProduct;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class CartProductsRE {
    @Getter
    @Setter
    private List<String> errors = new ArrayList<>();

    @Getter
    @Setter
    private List<CartProduct> cartProducts;

    public void addError(String error) {
        errors.add(error);
    }

    @Override
    public String toString() {
        return "CartProductsRE [cartProducts=" + cartProducts + ", errors=" + errors + "]";
    }

    
}
