package tqs.g11.zap.controller;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import tqs.g11.zap.auth.TokenProvider;
import tqs.g11.zap.auth.UnauthorizedEntryPoint;
import tqs.g11.zap.enums.UserRoles;
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.service.CartService;
import tqs.g11.zap.service.ProductService;
import tqs.g11.zap.service.UsersService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(RESTController.class)
class RESTControllerWithServiceMockTest {

    @Autowired
    private MockMvc mvc; 

    @MockBean
    private ProductService productService;

    @MockBean
    private CartService cartService;


    @MockBean
    private UsersService service2;

    @MockBean
    private UnauthorizedEntryPoint u;

    @MockBean
    private TokenProvider t;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User user1 = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);
    private User user2 = new User("user2", "Deinis Lie", "sussybot564", UserRoles.CLIENT);
    private User user3 = new User("user3", "Licius Vinicious", "edinaldus", UserRoles.CLIENT);

    @BeforeEach
    void setup() {

        Product p1 = new Product(1L, "Amogi Pen", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 4, user1, 15.5, "Pen Drive");
        Product p2 = new Product(2L, "USB Cable", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 3, user1, 3.0, "Cable");
        Product p3 = new Product(3L, "Charger 3", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 10, user1, 1000.0, "Charger");

        CartProduct cp1 = new CartProduct(1L, p1, 1, user2);
        CartProduct cp2 = new CartProduct(2L, p3, 3, user2);

        List<CartProduct> cpsUser2 = new ArrayList<>(Arrays.asList(cp1, cp2));

        CartProduct cp3 = new CartProduct(3L, p1, 1, user3);
        CartProduct cp4 = new CartProduct(4L, p2, 2, user3);
        CartProduct cp5 = new CartProduct(5L, p3, 1, user3);

        List<CartProduct> cpsUser3 = new ArrayList<>(Arrays.asList(cp3, cp4, cp5));

        ArrayList<Product> products = new ArrayList<>(Arrays.asList(p1, p2, p3));

        when(productService.getProducts()).thenReturn(products);
        when(productService.getProductById(1L)).thenReturn(Optional.of(p1));
        when(productService.createProduct(any(),any())).thenReturn(p1);

        System.out.println("sussy cpsUser2");
        System.out.println(cpsUser2);
        when(cartService.getCartsByUserId(2L)).thenReturn(cpsUser2);
        when(cartService.getCartsByUserId(3L)).thenReturn(cpsUser3);
        when(cartService.deleteCartsByUserId(2L)).thenReturn(cpsUser2);
        when(cartService.deleteCartsByUserId(3L)).thenReturn(cpsUser3);

    }

    @Test
    void getAllProducts() throws Exception{

        mvc.perform(
            get("/zap/products").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].productName", is("Amogi Pen")))
            .andExpect(jsonPath("$[0].owner.username", is(user1.getUsername())))
            .andExpect(jsonPath("$[1].productName", is("USB Cable")))
            .andExpect(jsonPath("$[1].owner.username", is(user1.getUsername())))
            .andExpect(jsonPath("$[2].productName", is("Charger 3")))
            .andExpect(jsonPath("$[2].owner.username", is(user1.getUsername())));
    }


    @Test
    void getProductById() throws Exception{

        mvc.perform(
            get("/zap/products/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productName", is("Amogi Pen")))
            .andExpect(jsonPath("$.owner.username", is(user1.getUsername())));
    }

    @Test
    void getCartProductsByUserId() throws Exception {
        mvc.perform(get("/zap/carts/user/2").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].quantity", is(1)))
            .andExpect(jsonPath("$[1].quantity", is(3)));

        mvc.perform(get("/zap/carts/user/3").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].quantity", is(1)))
            .andExpect(jsonPath("$[1].quantity", is(2)))
            .andExpect(jsonPath("$[2].quantity", is(1)));

    }

    @Test
    void deleteCartProductByUserId() throws Exception {
        mvc.perform(delete("/zap/carts/user/2").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].quantity", is(1)))
            .andExpect(jsonPath("$[1].quantity", is(3)));

        mvc.perform(delete("/zap/carts/user/3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].quantity", is(1)))
                .andExpect(jsonPath("$[1].quantity", is(2)))
                .andExpect(jsonPath("$[2].quantity", is(1)));
    }

    @Test
    void createProduct() throws Exception {
        Product p1 = new Product(1L, "Amogi Pen", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 4, user1, 15.5, "Pen Drive");

        String content = objectMapper.writeValueAsString(p1);
        System.out.println("Content");
        System.out.println(content);
        mvc.perform(post("/zap/products").contentType(MediaType.APPLICATION_JSON).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Amogi Pen")))
                .andExpect(jsonPath("$.owner.username", is(user1.getUsername())));
    }
}
