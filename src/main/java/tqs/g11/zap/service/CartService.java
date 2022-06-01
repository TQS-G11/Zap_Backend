package tqs.g11.zap.service;

import org.springframework.stereotype.Service;
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.repository.CartRepository;
import tqs.g11.zap.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<CartProduct> getCartsByUserId(Long id) {
        return null;
    }

    public List<CartProduct> deleteCartsByUserId(Long id) {
        return null;
    }

}
