package tqs.g11.zap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContains(String name);
    List<Product> findByCategoryContains(String category);
    List<Product> findByNameContainsAndCategoryContains(String name, String category);

    Optional<Product> getProductById(Long id);
}
