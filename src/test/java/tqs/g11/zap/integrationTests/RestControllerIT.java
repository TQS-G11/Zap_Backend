package tqs.g11.zap.integrationTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.TestPropertySource;

import tqs.g11.zap.enums.UserRoles;
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.repository.CartRepository;
import tqs.g11.zap.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.annotation.Before;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class RestControllerIT {

    @LocalServerPort
    int randomServerPort;

    // a REST client that is test-friendly
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CartRepository cartRep;

    @Autowired
    private ProductRepository productRep;


    @BeforeEach
    void setUp() {
        User user1 = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);
        User user2 = new User("user2", "Deinis Lie", "sussybot564", UserRoles.CLIENT);
        User user3 = new User("user3", "Licius Vinicious", "edinaldus", UserRoles.CLIENT);

        Product p1 = new Product(1L, "Among Us Pen Drive", "url1", "An Among Us pen drive", 69, user1, 420.69, "Pen Drive");
        Product p2 = new Product(2L, "Notebook super charger", "url2", "A notebook charger", 40, user1, 69.0, "Charger");
        Product p3 = new Product(3L, "Cellphone super charger", "url3", "An Among Us pen drive", 13, user1, 23.4, "Charger");

        CartProduct cp1 = new CartProduct(1L, p1, 1, user2);
        CartProduct cp2 = new CartProduct(2L, p3, 3, user2);

        List<CartProduct> cpsUser2 = new ArrayList<>(Arrays.asList(cp1, cp2));
    }

    @AfterEach
    void resetDb() {
        
    }

    @Test
    void checkoutCart(){

        ResponseEntity<String> response = restTemplate
            .exchange("zap/carts/user/1/checkout", HttpMethod.GET, null, String.class);

        //.exchange("/zap/carts/user/1/checkout", HttpMethod.GET, null); 

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Checkout Successful");
        

    }
    
}
