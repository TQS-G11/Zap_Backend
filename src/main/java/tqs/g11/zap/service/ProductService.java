package tqs.g11.zap.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final UsersService usersService;

    public ProductService(ProductRepository productRepository, UsersService usersService) {
        this.productRepository = productRepository;
        this.usersService = usersService;
    }

    public List<Product> filterProducts(String name, String category) {
        if (name == null && category == null) return getProducts();
        if (name == null) return getProductsByCategory(category);
        if (category == null) return getProductsByName(name);
        return getProductsByNameAndCategory(name, category);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.getProductByProductId(id);
    }

    public List<Product> getProductsByName(String name) { return productRepository.findByNameIgnoreCaseContaining(name); }

    public List<Product> getProductsByCategory(String name) { return productRepository.findByCategoryContains(name); }

    public List<Product> getProductsByNameAndCategory(String name, String category) {
        return productRepository.findByNameContainsAndCategoryContains(name, category);
    }

    public Product createProduct(Authentication auth, Product product) {
        User user = usersService.getAuthUser((UserDetails) auth.getPrincipal());
        product.setOwner(user);
        return productRepository.save(product);
    }
    
}
