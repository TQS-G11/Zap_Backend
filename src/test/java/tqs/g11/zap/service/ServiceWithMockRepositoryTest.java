package tqs.g11.zap.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tqs.g11.zap.dto.CartProductPost;
import tqs.g11.zap.dto.CartProductRE;
import tqs.g11.zap.enums.ErrorMsg;
import tqs.g11.zap.enums.UserRoles;
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.repository.CartRepository;
import tqs.g11.zap.repository.ProductRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceWithMockRepositoryTest {
    @Mock(lenient = true)
    private ProductRepository productRepository;

    @Mock(lenient = true)
    private CartRepository cartRepository;

    @InjectMocks
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    @Mock(lenient = true)
    private UsersService usersService;

    @InjectMocks
    private OrderService orderService;

    private User manager;

    private User client;

    private List<CartProduct> clientCartProducts;

    private Product product1;

    private Product product2;

    private Product product3;

    @BeforeEach
    void setUp() {
        manager = new User("caio_costela", "Caio Costela", "amogus123", UserRoles.MANAGER);

        User user1 = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);
        User user2 = new User("user2", "Deinis Lie", "sussybot564", UserRoles.CLIENT);
        User user3 = new User("user3", "Licius Vinicious", "edinaldus", UserRoles.CLIENT);

        Product p1 = new Product(1L, "Among Us Pen Drive", "url1", "An Among Us pen drive", 69, user1, 420.69, "Pen Drive");
        Product p2 = new Product(2L, "Notebook super charger", "url2", "A notebook charger", 40, user1, 69.0, "Charger");
        Product p3 = new Product(3L, "Cellphone super charger", "url3", "An Among Us pen drive", 13, user1, 23.4, "Charger");

        CartProduct cp1 = new CartProduct(1L, p1, 1, user2);
        CartProduct cp2 = new CartProduct(2L, p3, 3, user2);

        List<CartProduct> cpsUser2 = new ArrayList<>(Arrays.asList(cp1, cp2));

        CartProduct cp3 = new CartProduct(3L, p1, 1, user3);
        CartProduct cp4 = new CartProduct(4L, p2, 2, user3);
        CartProduct cp5 = new CartProduct(5L, p3, 1, user3);

        List<CartProduct> cpsUser3 = new ArrayList<>(Arrays.asList(cp3, cp4, cp5));

        product1 = new Product(200L, "Product 200", "img", "description",
                200, user1, 200.0, "category");
        product2 = new Product(201L, "Product 201", "img", "description",
                201, user1, 201.0, "category");
        product3 = new Product(202L, "Product 202", "img", "description",
                202, user1, 202.0, "category");
        client = new User("client", "A Client", "clientpassword", UserRoles.CLIENT);
        client.setId(100L);
        clientCartProducts = Arrays.asList(
                new CartProduct(101L, product1, 1, client),
                new CartProduct(102L, product2, 2, client)
        );
        when(cartRepository.findByUserId(client.getId())).thenReturn(clientCartProducts);
        Arrays.asList(product1, product2, product3).forEach(
                p -> when(productRepository.getProductByProductId(p.getProductId())).thenReturn(Optional.of(p))
        );


        when(productRepository.findByNameIgnoreCaseContaining("Among")).thenReturn(List.of(p1));
        when(productRepository.getProductByProductId(2L)).thenReturn(Optional.of(p2));
        when(productRepository.findByCategoryContains("Charger")).thenReturn(Arrays.asList(p2, p3));
        when(productRepository.findByNameContainsAndCategoryContains("Cellphone", "Charger")).thenReturn(List.of(p3));
        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));
        when(productRepository.save(any())).thenReturn(p1);

        when(cartRepository.findByUserId(2L)).thenReturn(cpsUser2);
        when(cartRepository.findByUserId(3L)).thenReturn(cpsUser3);
    }

    @Test
    void getAllProducts() {
        List<Product> products = productService.getProducts();

        assertThat(products).hasSize(3);
        assertThat(products.get(0).getName()).isEqualTo("Among Us Pen Drive");
    }

    @Test
    void getProductById() {
        Optional<Product> product = productService.getProductById(2L);

        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("Notebook super charger");

    }

    @Test
    void getProductByName() {
        List<Product> products = productService.getProductsByName("Among");

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Among Us Pen Drive");
    }

    @Test
    void getProductByCategory() {
        List<Product> products = productService.getProductsByCategory("Charger");

        assertThat(products).hasSize(2);
        assertThat(products.get(0).getName()).isEqualTo("Notebook super charger");
        assertThat(products.get(1).getName()).isEqualTo("Cellphone super charger");

    }

    @Test
    void getProductByNameAndCategory() {
        List<Product> products = productService.getProductsByNameAndCategory("Cellphone", "Charger");

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Cellphone super charger");
    }

    @Test
    void createProduct() {

        Authentication auth = setUpUserMockAuth(manager);

        Product p1 = new Product(1L, "Among Us Pen Drive", "url1", "An Among Us pen drive", 69, null, 420.69, "Pen Drive");

        Product output = productService.createProduct(auth, p1);

        assertThat(output.getName()).isEqualTo("Among Us Pen Drive");
        assertThat(output.getOwner().getName()).isEqualTo("Caio Costela");
    }

    @Test
    void getCartProductByUserId() {
        List<CartProduct> cartProducts = cartService.getCartsByUserId(2L);
        assertThat(cartProducts).hasSize(2);

        cartProducts = cartService.getCartsByUserId(3L);
        assertThat(cartProducts).hasSize(3);
    }

    @Test
    void deleteCartProductByUserId() {
        List<CartProduct> cartProducts = cartService.deleteCartsByUserId(2L);
        assertThat(cartProducts).hasSize(2);

        cartProducts = cartService.deleteCartsByUserId(3L);
        assertThat(cartProducts).hasSize(3);
    }

    @Test
    void testGetClientCart() {
        Authentication auth = setUpUserMockAuth(client);
        List<CartProduct> cart = cartService.getClientCart(auth);
        assertThat(cart).isEqualTo(clientCartProducts);
    }

    @Test
    void testClientAddCartProductOk() {
        cartService = new CartService(cartRepository, usersService, productService, orderService);
        Authentication auth = setUpUserMockAuth(client);
        CartProductPost postBody = new CartProductPost(product3.getProductId(), 1);
        ResponseEntity<CartProductRE> re = cartService.clientAddCartProduct(auth, postBody);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void testClientAddCartProductNotEnoughStock() {
        cartService = new CartService(cartRepository, usersService, productService, orderService);
        Authentication auth = setUpUserMockAuth(client);
        CartProductPost postBody = new CartProductPost(product3.getProductId(), 1000);
        ResponseEntity<CartProductRE> re = cartService.clientAddCartProduct(auth, postBody);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(Objects.requireNonNull(re.getBody()).getErrors())
                .isEqualTo(List.of(ErrorMsg.PRODUCT_NOT_ENOUGH_STOCK.toString()));
    }

    private Authentication setUpUserMockAuth(User user) {
        Authentication auth = mock(Authentication.class);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities
        );
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(usersService.getAuthUser(userDetails)).thenReturn(user);
        return auth;
    }
}
