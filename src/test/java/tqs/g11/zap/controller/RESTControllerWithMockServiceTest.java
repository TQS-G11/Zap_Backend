package tqs.g11.zap.controller;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import tqs.g11.zap.auth.TokenProvider;
import tqs.g11.zap.auth.UnauthorizedEntryPoint;
import tqs.g11.zap.enums.UserRoles;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.service.ProductService;
import tqs.g11.zap.service.UsersService;

import java.util.Optional;

@WebMvcTest(RESTController.class)
class RESTControllerWithServiceMockTest {

    @Autowired
    private MockMvc mvc; 

    @MockBean
    private ProductService service;

    @MockBean
    private UsersService service2;

    @MockBean
    private UnauthorizedEntryPoint u;

    @MockBean
    private TokenProvider t;

    @Test
    void getAllProducts() throws Exception{

        User user = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);
        ArrayList<Product> products = new ArrayList<>(){
            {
                add(new Product("Amogi Pen", "", user));
                add(new Product("Charger 3", "", user));
                add(new Product("AmogusPen", "", user));
            }
        }; 

        when(service.getProducts()).thenReturn(products);

        mvc.perform(
            get("/zap/products").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].productName", is("Amogi Pen")))
            .andExpect(jsonPath("$[0].owner.username", is(user.getUsername())))
            .andExpect(jsonPath("$[1].productName", is("Charger 3")))
            .andExpect(jsonPath("$[1].owner.username", is(user.getUsername())))
            .andExpect(jsonPath("$[2].productName", is("AmogusPen")))
            .andExpect(jsonPath("$[2].owner.username", is(user.getUsername())));
    }


    @Test
    void getProductById() throws Exception{

        
        User user = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);

        Product testProduct = new Product("Amogi Pen", "", user);


        when(service.getProductById(1L)).thenReturn(Optional.of(testProduct));

        mvc.perform(
            get("/zap/products/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productName", is("Amogi Pen")))
            .andExpect(jsonPath("$.owner.username", is(user.getUsername())));
    }
}
