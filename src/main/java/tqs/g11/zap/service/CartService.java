package tqs.g11.zap.service;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tqs.g11.zap.dto.CartProductPost;
import tqs.g11.zap.dto.CartProductRE;
import tqs.g11.zap.dto.CartProductsRE;
import tqs.g11.zap.enums.ErrorMsg;
import tqs.g11.zap.enums.UserRoles;
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.repository.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;

    private final UsersService usersService;

    private final ProductService productService;

    public CartService(CartRepository cartRepository, UsersService usersService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.usersService = usersService;
        this.productService = productService;
    }

    public List<CartProduct> getCartsByUserId(Long id) {
        return cartRepository.findByUserId(id);
    }

    public List<CartProduct> getCartsByUser(User user) {
        return cartRepository.findByUserId(user.getId());
    }

    public List<CartProduct> deleteCartsByUserId(Long id) {
        List<CartProduct> cartProducts = this.getCartsByUserId(id);
        cartRepository.deleteAll(cartProducts);
        return cartProducts;
    }

    public List<CartProduct> getClientCart(Authentication auth) {
        User client = usersService.getAuthUser((UserDetails) auth.getPrincipal());
        assert client.getRole().equals(UserRoles.CLIENT.toString());
        return getCartsByUserId(client.getId());
    }

    public ResponseEntity<CartProductRE> clientAddCartProduct(Authentication auth, CartProductPost cartProductPost) {
        CartProductRE re = new CartProductRE();
        User client = usersService.getAuthUser((UserDetails) auth.getPrincipal());
        assert client.getRole().equals(UserRoles.CLIENT.toString());
        Optional<Product> productOpt = productService.getProductById(cartProductPost.getProductId());
        Product product = null;
        if (productOpt.isEmpty())
            re.addError(ErrorMsg.PRODUCT_NOT_FOUND.toString());
        else {
            product = productOpt.get();
            if (product.getQuantity() < cartProductPost.getQuantity())
                re.addError(ErrorMsg.PRODUCT_NOT_ENOUGH_STOCK.toString());
        }

        if (re.getErrors().isEmpty()) {
            CartProduct cartProduct = new CartProduct(product, cartProductPost.getQuantity(), client);
            re.setCartProduct(cartProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(re);
        }

        return ResponseEntity.badRequest().body(re);
    }

    public ResponseEntity<CartProductsRE> clientCartCheckout(Authentication auth) {
        CartProductsRE re = new CartProductsRE();
        User client = usersService.getAuthUser((UserDetails) auth.getPrincipal());
        assert client.getRole().equals(UserRoles.CLIENT.toString());
        List<CartProduct> cart = getCartsByUser(client);
        cart.forEach((cartProduct -> {
            if (cartProduct.getQuantity() > cartProduct.getProduct().getQuantity())
                re.addError(ErrorMsg.PRODUCT_NOT_ENOUGH_STOCK + " (ID: "
                        + cartProduct.getProduct().getProductId() + ")");
        }));

        if (re.getErrors().isEmpty()) {
            // TODO: POST request to Deliverize to create order
        }

        return ResponseEntity.badRequest().body(re);
    }
}
