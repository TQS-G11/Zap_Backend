package tqs.g11.zap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.g11.zap.model.Product;
import tqs.g11.zap.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.getProductByProductId(id);
    }

    public List<Product> getProductsByName(String name) { return productRepository.findByProductNameIgnoreCaseContaining(name); }

    public List<Product> getProductsByCategory(String name) { return productRepository.findByCategoryContains(name); }

    public List<Product> getProductsByNameAndCategory(String name, String category) {
        return productRepository.findByProductNameContainsAndCategoryContains(name, category);
    }

    public Product createProduct(Product product) {
        return null;
    }
    
}
