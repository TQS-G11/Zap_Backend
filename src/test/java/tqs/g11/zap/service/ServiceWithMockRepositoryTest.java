package tqs.g11.zap.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
public class ServiceWithMockRepositoryTest {

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

    private User manager;

//    @Mock
//    private UsersService usersService;

    @BeforeEach
//    @WithMockUser(username = "sussycosta", roles = {"CLIENT"})
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

        assertThat(product.isEmpty()).isFalse();
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

        Product output = productService.createProduct(auth,p1);

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
