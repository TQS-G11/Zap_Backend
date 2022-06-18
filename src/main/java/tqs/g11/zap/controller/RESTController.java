package tqs.g11.zap.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import tqs.g11.zap.dto.CartProductPost;
import tqs.g11.zap.dto.CartProductRE;
import tqs.g11.zap.dto.CartProductsRE;
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.service.CartService;
import tqs.g11.zap.service.ProductService;

@RestController
@CrossOrigin("*")
@RequestMapping("/zap")
public class RESTController {
    

    private final ProductService productService;
    private final CartService cartService;

    public RESTController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @Operation(summary = "Fetch all products available on the store")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products Found")
    })
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> data = productService.getProducts();
        return ResponseEntity.ok().body(data);
    }

    @Operation(summary = "Fetch a specific product by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product Found")
    })
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Optional<Product> data = productService.getProductById(id);
        return ResponseEntity.ok().body(data.get());
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(Authentication auth, @RequestBody Product product) {
        Product data = productService.createProduct(auth, product);
        return new ResponseEntity<Product>(data, HttpStatus.CREATED);
    }


    @Operation(summary = "Fetch the cart of a specific User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart Found"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated."),
        @ApiResponse(responseCode = "403", description = "Unauthorized (not the correct User).")
    })
    @GetMapping("/carts/user/{user_id}")
    public ResponseEntity<List<CartProduct>> getCartsByUserId(@PathVariable("user_id") Long userId) {
        List<CartProduct> cartProducts = cartService.getCartsByUserId(userId);
        return ResponseEntity.ok().body(cartProducts);
    }

    @Operation(summary = "Delete the cart of a specific User")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cart Deleted"),
        @ApiResponse(responseCode = "401", description = "Unauthenticated."),
        @ApiResponse(responseCode = "403", description = "Unauthorized (not the correct User).")
    })
    @DeleteMapping("/carts/user/{user_id}")
    public ResponseEntity<List<CartProduct>> deleteCartsByUserId(@PathVariable("user_id") Long userId) {
        List<CartProduct> cartProducts = cartService.deleteCartsByUserId(userId);
        return ResponseEntity.ok().body(cartProducts);
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/cart")
    public ResponseEntity<List<CartProduct>> getUserCart(Authentication auth) {
        List<CartProduct> cartProducts = cartService.getClientCart(auth);
        return ResponseEntity.ok().body(cartProducts);
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @PostMapping("/cart/add")
    public ResponseEntity<CartProductRE> clientAddCartProduct(Authentication auth, @RequestBody CartProductPost cartProductPost) {
        return cartService.clientAddCartProduct(auth, cartProductPost);
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @GetMapping("/cart/checkout")
    public ResponseEntity<CartProductsRE> clientCartCheckout(Authentication auth) {
        return cartService.clientCartCheckout(auth);
    }

    @PreAuthorize("hasAnyRole('CLIENT')")
    @DeleteMapping("/cart/{cart_id}")
    public ResponseEntity<CartProductRE> clientDeleteCart(Authentication auth, @PathVariable("cart_id") Long cartId) {
        return cartService.deleteCartById(auth, cartId);
    }
}
