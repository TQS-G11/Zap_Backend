package tqs.g11.zap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.g11.zap.data.Product;
import tqs.g11.zap.service.ZapService;

//@CrossOrigin
@RestController
@RequestMapping("/zap")
public class RESTController {
    

    @Autowired
    private ZapService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAlLProducts() {
        List<Product> data = service.getProducts();
        return ResponseEntity.ok().body(data);
    }
    
    @GetMapping("/products?id={int}")
    public ResponseEntity<List<Product>> getProductById() {
        List<Product> data = service.getProducts();
        return ResponseEntity.ok().body(data);
    } 

}
