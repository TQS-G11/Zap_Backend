package tqs.g11.zap.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.g11.zap.model.Product;
import tqs.g11.zap.service.ProductService;

//@CrossOrigin
@RestController
@RequestMapping("/zap")
public class RESTController {
    

    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> data = service.getProducts();
        return ResponseEntity.ok().body(data);
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Optional<Product> data = service.getProductById(id);
        return ResponseEntity.ok().body(data.get());
    } 

}
