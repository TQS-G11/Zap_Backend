package tqs.g11.zap.controller;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import tqs.g11.zap.auth.DefaultPasswordEncoder;
import tqs.g11.zap.auth.JwtAuthenticationFilter;
import tqs.g11.zap.auth.TokenProvider;
import tqs.g11.zap.auth.UnauthorizedEntryPoint;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.service.ZapService;
import tqs.g11.zap.service.UsersService;

@WebMvcTest(RESTController.class)
class RESTControllerWithServiceMockTest {

    @Autowired
    private MockMvc mvc; 

    @MockBean
    private ZapService service;

    @MockBean
    private UsersService service2;

    @MockBean
    private UnauthorizedEntryPoint u;

    @MockBean
    private TokenProvider t;

    @Test
    void getAllProducts() throws Exception{

        ArrayList<Product> products = new ArrayList<>(){
            {
                add(new Product("Amogi Pen", "", 4));
                add(new Product("Charger 3", "", 7));
                add(new Product("AmogusPen", "", 3));
            }
        }; 

        when(service.getProducts()).thenReturn(products);

        mvc.perform(
            get("/zap/products").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].productName", is("Amogi Pen")))
            .andExpect(jsonPath("$[0].ownerId", is(4)))
            .andExpect(jsonPath("$[1].productName", is("Charger 3")))
            .andExpect(jsonPath("$[1].ownerId", is(7)))
            .andExpect(jsonPath("$[2].productName", is("AmogusPen")))
            .andExpect(jsonPath("$[2].ownerId", is(3)));
    }


    @Test
    void getProductById() throws Exception{

        Product testProduct = new Product("Amogi Pen", "", 4);

        when(service.getProductById(1l)).thenReturn(testProduct);

        mvc.perform(
            get("/zap/products/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.productName", is("Amogi Pen")))
            .andExpect(jsonPath("$.ownerId", is(4)));
    }
}
