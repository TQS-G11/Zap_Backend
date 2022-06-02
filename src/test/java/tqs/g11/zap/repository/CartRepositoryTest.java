package tqs.g11.zap.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.g11.zap.enums.UserRoles;
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;

@DataJpaTest
class CartRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;

    private User user = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);

    private Product p1 = new Product("Amogi Pen", "", user);
    private Product p2 = new Product("USB Cable", "", user);
    private Product p3 = new Product("Charger 3", "", user);

    private CartProduct cp1 = new CartProduct(p1, 1, user);
    private CartProduct cp2 = new CartProduct(p2, 3, user);
    private CartProduct cp3 = new CartProduct(p3, 3, user);

    @Autowired
    private CartRepository repository;

    @BeforeEach
    void setUp(){

        p1.setCategory("USB");            
        p2.setCategory("Charger");        
        p3.setCategory("USB");            

        entityManager.persist(user);
        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.persist(p3);
        entityManager.persist(cp1);
        entityManager.persist(cp2);
        entityManager.persist(cp3);
        entityManager.flush();
    }
    
    @AfterEach
    void clear(){
        entityManager.clear();
    }
      

    @Test
    void findByUserIdTest(){

        List<CartProduct> cart = repository.findByUserId(user.getId());

        assertThat(cart)
            .hasSize(3)
            .extracting(CartProduct::getId, CartProduct::getUser, CartProduct::getProduct)
            .containsOnly(
                tuple(cp1.getId(), user, p1),
                tuple(cp2.getId(), user, p2),
                tuple(cp3.getId(), user, p3)
            );

        entityManager.clear();

    }

    @Test
    void findById(){
        Optional<CartProduct> cart1 = repository.findById(cp1.getId());

        assertThat(cart1)
            .isNotEmpty()
            .contains(cp1);

        Optional<CartProduct> cart2 = repository.findById(-1l);

        assertThat(cart2)
            .isEmpty();
        entityManager.clear();
    }

}
