package tqs.g11.zap.integrationTests;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.TestPropertySource;

import tqs.g11.zap.dto.AuthToken;
import tqs.g11.zap.dto.CartCheckoutPostDTO;
import tqs.g11.zap.dto.CartProductsRE;
import tqs.g11.zap.dto.LoginRE;
import tqs.g11.zap.dto.LoginUser;
import tqs.g11.zap.dto.SignupRE;
import tqs.g11.zap.dto.UserDto;
import tqs.g11.zap.enums.UserRoles;

import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.repository.CartRepository;
import tqs.g11.zap.repository.ProductRepository;
import tqs.g11.zap.repository.UsersRepository;

import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;


import tqs.g11.zap.service.UsersService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
;

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

    @Autowired
    private UsersRepository usersRep;

    @Autowired
    private UsersService usersService;

    private User user1 = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);
    private User user2 = new User("user2", "Deinis Lie", "sussybot564", UserRoles.CLIENT);
    private User user3 = new User("user3", "Licius Vinicious", "edinaldus", UserRoles.CLIENT);

    private Product p1 = new Product(1L, "Among Us Pen Drive", 
                                    "url1", "An Among Us pen drive", 69, 
                                    user1, 420.69, "Pen Drive"
    );
    private Product p2 = new Product(2L, "Notebook super charger", 
                                    "url2", "A notebook charger", 40, 
                                    user1, 69.0, "Charger"
    );
    private Product p3 = new Product(3L, "Cellphone super charger", 
                                    "url3", "An Among Us pen drive", 13, 
                                    user1, 23.4, "Charger"
    );

    private AuthToken user1AuthToken;
    private AuthToken user2AuthToken;


    @Test
    void runTests() throws JsonProcessingException{
        signUp();
        login();
        //createProducts();
        //getProducts();
        //getProductById();
        checkoutCart();
    }


    void signUp() throws JsonProcessingException{
        UserDto newUser = new UserDto(user1);
        ResponseEntity<SignupRE> response = restTemplate.postForEntity("/api/users/signup", newUser, SignupRE.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<User> found = usersRep.findAll();
        assertThat(found).extracting(User::getName).contains(user1.getName());

        newUser = new UserDto(user2);
        response = restTemplate.postForEntity("/api/users/signup", newUser, SignupRE.class);
        newUser = new UserDto(user3);
        response = restTemplate.postForEntity("/api/users/signup", newUser, SignupRE.class);

    }

    void login() throws JsonProcessingException{
        LoginUser credentials1 = new LoginUser(user1.getUsername(), user1.getPassword());
        LoginUser credentials2 = new LoginUser(user1.getUsername(), "");
        LoginUser credentials3 = new LoginUser("", user1.getPassword());
        LoginUser credentials4 = new LoginUser("Name", "Password");
        LoginUser credentials5 = new LoginUser(user2.getUsername(), user2.getPassword());

        // Blank password
        ResponseEntity<LoginRE> response = restTemplate.postForEntity("/api/users/login", credentials2, LoginRE.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        List<String> errors = response.getBody().getErrors();
        assertThat(errors).contains("Password field cannot be blank.", "Invalid credentials.");

        // Blank Username
        response = restTemplate.postForEntity("/api/users/login", credentials3, LoginRE.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        errors = response.getBody().getErrors();
        assertThat(errors).contains("Username field cannot be blank.", "Invalid credentials.");

        // Invalid Username/Password
        response = restTemplate.postForEntity("/api/users/login", credentials4, LoginRE.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        errors = response.getBody().getErrors();
        assertThat(errors).contains("Invalid credentials.");

        // Correct Credentials
        response = restTemplate.postForEntity("/api/users/login", credentials1, LoginRE.class);
        errors = response.getBody().getErrors();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(errors).isEmpty();

        user1AuthToken = response.getBody().getToken();

        assertThat(user1AuthToken).isNotNull();

        response = restTemplate.postForEntity("/api/users/login", credentials5, LoginRE.class);
        user2AuthToken = response.getBody().getToken();
        assertThat(user2AuthToken).isNotNull();
    }

    void createProducts(){

        ResponseEntity<Product> response = restTemplate.postForEntity("/zap/products", p1, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user1AuthToken.getToken());

        Map<String, Product> map = new HashMap<>();
        map.put("product", p1);
        HttpEntity<Map<String,Product>> request = new HttpEntity(map, headers);
        

        response = restTemplate.postForEntity("/zap/products", request, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getProductId()).isEqualTo(p1.getProductId());

        map.put("product", p2);
        request = new HttpEntity<>(map, headers);

        response = restTemplate.postForEntity("/zap/products", request, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getProductId()).isEqualTo(p2.getProductId());


        map.put("product", p2);
        request = new HttpEntity<>(map, headers);

        response = restTemplate.postForEntity("/zap/products", request, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getProductId()).isEqualTo(p3.getProductId());
    }


    void getProducts() {

        ResponseEntity<List<Product>> response = restTemplate.exchange(
                                                            "/zap/products",
                                                            HttpMethod.GET,
                                                            null,
                                                            new ParameterizedTypeReference<List<Product>>(){}
        );

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody())
            .extracting(Product::getProductId)
            .contains(p1.getProductId(),p2.getProductId(),p3.getProductId());
    }


    void getProductById(){
        ResponseEntity<Product> response = restTemplate.exchange(
            "/zap/products/1",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Product>(){}
        );

        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getBody().getProductId())
            .isEqualTo(p1.getProductId());

    }

    void checkoutCart(){

        HttpHeaders headers = new HttpHeaders();
        
        CartCheckoutPostDTO details = new CartCheckoutPostDTO("Aveiro", "Hello :)");
        System.out.println(details.getDestination());
        Map<String, String> map = new HashMap<>();
        //map.put("cartCheckoutPostDTO", details);
        map.put("destination", "Aveiro");
        map.put("notes", "Hello :)");

        HttpEntity<Map<String,CartCheckoutPostDTO>> request = new HttpEntity(map);

        ResponseEntity<CartProductsRE> response = restTemplate.postForEntity("/zap/cart/checkout", request, CartProductsRE.class);
            
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        headers.setBearerAuth(user2AuthToken.getToken());
        request = new HttpEntity(map, headers);
        //System.out.println(request.getBody().get("cartCheckoutPostDTO").getDestination());
        response = restTemplate.postForEntity("/zap/cart/checkout", request, CartProductsRE.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        //assertThat(response.getBody()).contains("Checkout Successful");
    
    }
    
}
