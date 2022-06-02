package tqs.g11.zap.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.g11.zap.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>  {
    
    List<Product> findByProductNameIgnoreCaseContaining(String name);

}
