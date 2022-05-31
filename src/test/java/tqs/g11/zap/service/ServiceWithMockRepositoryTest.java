package tqs.g11.zap.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.g11.zap.enums.UserRoles;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.repository.ProductRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceWithMockRepositoryTest {

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Mock
    private UsersService usersService;

    @BeforeEach
//    @WithMockUser(username = "sussycosta", roles = {"CLIENT"})
    void setUp() {
        User user = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);
//        when(usersService.getAuthUser(any())).thenReturn(mockUser);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = usersService.getAuthUser((UserDetails) authentication.getPrincipal());
        Product p1 = new Product(1L, "Among Us Pen Drive", "url1", "An Among Us pen drive", 69, user, 420.69, "Pen Drive");
        Product p2 = new Product(2L, "Notebook super charger", "url2", "A notebook charger", 40, user, 69.0, "Charger");
        Product p3 = new Product(3L, "Cellphone super charger", "url3", "An Among Us pen drive", 13, user, 23.4, "Charger");

        when(productRepository.findByNameContains("Among")).thenReturn(List.of(p1));
        when(productRepository.getProductById(2L)).thenReturn(Optional.of(p2));
        when(productRepository.findByCategoryContains("Charger")).thenReturn(Arrays.asList(p2, p3));
        when(productRepository.findByNameContainsAndCategoryContains("Cellphone", "Charger")).thenReturn(List.of(p3));
        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));
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
}
