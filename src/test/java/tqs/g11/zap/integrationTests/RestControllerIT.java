package tqs.g11.zap.integrationTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.repository.CartRepository;
import tqs.g11.zap.repository.ProductRepository;
import tqs.g11.zap.repository.UsersRepository;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import tqs.g11.zap.auth.TokenProvider;
import tqs.g11.zap.auth.UnauthorizedEntryPoint;

import tqs.g11.zap.enums.ErrorMsg;

import tqs.g11.zap.service.CartService;
import tqs.g11.zap.service.OrderService;
import tqs.g11.zap.service.ProductService;
import tqs.g11.zap.service.UsersService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private UsersRepository usersRep;

    @Autowired
    private UsersService usersService;

    User user1 = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);
    User user2 = new User("user2", "Deinis Lie", "sussybot564", UserRoles.CLIENT);
    User user3 = new User("user3", "Licius Vinicious", "edinaldus", UserRoles.CLIENT);

    Product p1 = new Product(1L, "Among Us Pen Drive", "url1", "An Among Us pen drive", 69, user1, 420.69, "Pen Drive");
    Product p2 = new Product(2L, "Notebook super charger", "url2", "A notebook charger", 40, user1, 69.0, "Charger");
    Product p3 = new Product(3L, "Cellphone super charger", "url3", "An Among Us pen drive", 13, user1, 23.4, "Charger");



    AuthToken authToken;

    private ObjectMapper objectMapper = new ObjectMapper();


    
    void setUp() {
        productRep.save(p1);
        productRep.save(p2);
        productRep.save(p3);
        productRep.flush();

        CartProduct cp1 = new CartProduct(1L, p1, 1, user2);
        CartProduct cp2 = new CartProduct(2L, p3, 3, user2);

        List<CartProduct> cpsUser2 = new ArrayList<>(Arrays.asList(cp1, cp2));
    }

    @AfterEach
    void resetDb() {
        
    }

    @Test
    void runTests() throws JsonProcessingException{
        signUp();
        login();
        createProducts();
        getProducts();
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

        authToken = response.getBody().getToken();

        assertThat(authToken).isNotNull();
    }

    void createProducts(){

        ResponseEntity<Product> response = restTemplate.postForEntity("/zap/products", p1, Product.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        HttpHeaders headers = new HttpHeaders();


        headers.setBearerAuth(authToken.getToken());

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

    // @Test
    // void checkoutCart(){

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     //Sign-up/Login
        

    //     //Authentication auth = setUpUserMockAuth(user1);

    //     //headers.setBearerAuth(auth.toString());
    //     //System.out.println(auth);


    //     CartCheckoutPostDTO details = new CartCheckoutPostDTO("Aveiro", "Hello :)");
    //     ResponseEntity<CartProductsRE> response = restTemplate.postForEntity("/zap/cart/checkout", details, CartProductsRE.class);
            
    //     assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    //     //assertThat(response.getBody()).contains("Checkout Successful");
    
    // }
    
}
