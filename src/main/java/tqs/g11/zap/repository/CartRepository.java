package tqs.g11.zap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tqs.g11.zap.model.CartProduct;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findByUserId(Long id);

    @Override
    Optional<CartProduct> findById(Long aLong);
}
