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

import tqs.g11.zap.data.Product;
import tqs.g11.zap.service.ZapService;

@WebMvcTest(RESTController.class)
class RESTControllerWithServiceMockTest {

    @Autowired
    private MockMvc mvc; 

    @MockBean
    private ZapService service;


    @BeforeEach
    void setUp(){

        ArrayList<Product> products = new ArrayList<>(){
            {
                add(new Product(1, "Amogi Pen", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 4, 1, 15.5));
                add(new Product(1, "USB Cable", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 3, 1, 3));
                add(new Product(1, "Charger 3", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 10, 1, 1000));
            }
        }; 

        when(service.getProducts()).thenReturn(products);
    }


    @Test
    void getAllProducts() throws Exception{

        mvc.perform(
            get("/zap/products").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name", is("Amogi Pen")))
            .andExpect(jsonPath("$[0].quantity", is(4)))
            .andExpect(jsonPath("$[1].name", is("USB Cable")))
            .andExpect(jsonPath("$[1].quantity", is(3)))
            .andExpect(jsonPath("$[2].name", is("Charger 3")))
            .andExpect(jsonPath("$[2].quantity", is(10)));
    }




}
