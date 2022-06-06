package tqs.g11.zap.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.service.CartService;
import tqs.g11.zap.service.ProductService;

//@CrossOrigin
@RestController
@RequestMapping("/zap")
public class RESTController {
    

    private final ProductService productService;
    private final CartService cartService;

    public RESTController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> data = productService.getProducts();
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Optional<Product> data = productService.getProductById(id);
        return ResponseEntity.ok().body(data.get());
    }

    @GetMapping("/carts/user/{user_id}")
    public ResponseEntity<List<CartProduct>> getCartsByUserId(@PathVariable("user_id") Long userId) {
        List<CartProduct> cartProducts = cartService.getCartsByUserId(userId);
        return ResponseEntity.ok().body(cartProducts);
    }

    @DeleteMapping("/carts/user/{user_id}")
    public ResponseEntity<List<CartProduct>> deleteCartsByUserId(@PathVariable("user_id") Long userId) {
        List<CartProduct> cartProducts = cartService.deleteCartsByUserId(userId);
        return ResponseEntity.ok().body(cartProducts);
    }


}
