package tqs.g11.zap.service;

import org.springframework.stereotype.Service;

import tqs.g11.zap.client.TqsBasicHttpClient;
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.repository.CartRepository;
import tqs.g11.zap.repository.ProductRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final TqsBasicHttpClient client;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
        this.client = new TqsBasicHttpClient();
    }

    public List<CartProduct> getCartsByUserId(Long id) {
        return cartRepository.findByUserId(id);
    }

    public List<CartProduct> deleteCartsByUserId(Long id) {
        List<CartProduct> cartProducts = this.getCartsByUserId(id);
        cartRepository.deleteAll(cartProducts);
        return cartProducts;
    }

    public Boolean checkoutCart(Long id) throws IOException{

        List<CartProduct> cart = cartRepository.findByUserId(id);

        // No products to checkout
        if( cart.isEmpty()){
            return false;
        }

        // Find Available Rider
        // TODO: Make Request match deliverize 
        String response = client.doHttpGet("");

        // TODO: Make response match deliverize
        if (response == "Invalid"){
            return false;
        }

        return true;
    }

}
