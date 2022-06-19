package tqs.g11.zap.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tqs.g11.zap.client.TqsBasicHttpClient;
import tqs.g11.zap.dto.CartProductPost;
import tqs.g11.zap.dto.CartProductRE;
import tqs.g11.zap.dto.CartProductsRE;
import tqs.g11.zap.dto.LoginUser;
import tqs.g11.zap.enums.ErrorMsg;
import tqs.g11.zap.enums.UserRoles;
import tqs.g11.zap.model.*;
import tqs.g11.zap.repository.CartRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;

    private final UsersService usersService;

    private final ProductService productService;

    private final OrderService orderService;

    private static final Double storeLat = 40.62708219296578;
    private static final Double storeLon = -8.64542661755792;
    private static final String storeName = "ZAP - Glicínias, Aveiro";
    private static final String storeUsername = "Zap";
    private static final String storePassword = "zapogus123";

    private static final String deliverizeBaseURI = "http://deliverizebackend:8080";
    private static final String deliverizeLogin = deliverizeBaseURI + "/api/users/login";
    private static final String deliverizeOrder = deliverizeBaseURI + "/api/deliveries/company";
    public CartService(CartRepository cartRepository, UsersService usersService, ProductService productService,
       OrderService orderService) {
        this.cartRepository = cartRepository;
        this.usersService = usersService;
        this.productService = productService;
        this.orderService = orderService;
    }

    public Optional<CartProduct> getCartById(Long id) { return cartRepository.findById(id); }

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
            cartRepository.save(cartProduct);
            re.setCartProduct(cartProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(re);
        }

        return ResponseEntity.badRequest().body(re);
    }

    public ResponseEntity<CartProductsRE> clientCartCheckout(Authentication auth, CartCheckoutPostDTO cartCheckoutPostDTO) throws IOException {
        CartProductsRE re = new CartProductsRE();
        System.out.println("Sussy test");
        User client = usersService.getAuthUser((UserDetails) auth.getPrincipal());
        assert client.getRole().equals(UserRoles.CLIENT.toString());
        List<CartProduct> cart = getCartsByUser(client);
        cart.forEach((cartProduct -> {
            if (cartProduct.getQuantity() > cartProduct.getProduct().getQuantity())
                re.addError(ErrorMsg.PRODUCT_NOT_ENOUGH_STOCK + " (ID: "
                        + cartProduct.getProduct().getProductId() + ")");
        }));

        if (cartCheckoutPostDTO.getDestination().equals("") || cartCheckoutPostDTO.getDestination() == null) {
            re.addError(ErrorMsg.DESTINATION_NOT_GIVEN.toString());
        }

        if (cart.isEmpty()) {
            re.addError(ErrorMsg.NOT_CART_PRODUCT.toString());
        }

        if (re.getErrors().isEmpty()) {
            TqsBasicHttpClient httpClient = new TqsBasicHttpClient();
            String username = client.getUsername();
            LoginUser loginUser = new LoginUser(storeUsername, storePassword);
            JsonObject loginResponse = httpClient.doHttpPost(deliverizeLogin, loginUser, null);
            String token = loginResponse.getAsJsonObject("token").get("token").getAsString();

            OrderPostDTO orderPostDTO = new OrderPostDTO(username, cartCheckoutPostDTO.getDestination(),
                cartCheckoutPostDTO.getNotes(), storeName, storeLat, storeLon);
            JsonObject orderResponse = httpClient.doHttpPost(deliverizeOrder, orderPostDTO, token);
            System.out.println("orderResponse");
            System.out.println(orderResponse.toString());
            JsonArray errors = orderResponse.getAsJsonArray("errors");
            if (errors.size() == 0) {
                re.setCartProducts(cart);
                JsonObject jsonOrder = orderResponse.getAsJsonObject("orderDto");
                Long orderId = jsonOrder.get("id").getAsLong();

                Order order = new Order(orderId, client);
                orderService.save(order);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(re);
            }
        }

        return ResponseEntity.badRequest().body(re);
    }

    public ResponseEntity<CartProductRE> deleteCartById(Authentication auth, Long cartId) {
        CartProductRE re = new CartProductRE();
        User client = usersService.getAuthUser((UserDetails) auth.getPrincipal());
        assert client.getRole().equals(UserRoles.CLIENT.toString());
        Optional<CartProduct> cartOpt = getCartById(cartId);
        if (cartOpt.isEmpty()) {
            re.addError(ErrorMsg.CART_PRODUCT_NOT_FOUND.toString());
        } else {
            CartProduct cart = cartOpt.get();
            if (!cart.getUser().equals(client)) {
                re.addError(ErrorMsg.WRONG_CLIENT_FOR_CART_PRODUCT.toString());
            }

            if (re.getErrors().isEmpty()) {
                re.setCartProduct(cart);
                cartRepository.delete(cart);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(re);
            }
        }

        return ResponseEntity.badRequest().body(re);
    }
}
