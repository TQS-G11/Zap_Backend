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
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;


@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository repository;

    private User user = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);

    private Product test1 = new Product("Amogi Pen", "", user);
    private Product test2 = new Product("Charger 3", "", user);
    private Product test3 = new Product("AmogusPen", "", user);


    @BeforeEach
    void setUp(){

        test1.setCategory("USB");            
        test2.setCategory("Charger");        
        test3.setCategory("USB");            

        entityManager.persist(user);
        entityManager.persist(test1);
        entityManager.persist(test2);
        entityManager.persist(test3);
        entityManager.flush();
    }
    
    @AfterEach
    void clear(){
        entityManager.clear();
    }

    @Test
    void getProductByIdTest() {
        // test if product exists
        Product found = repository.findById(test1.getProductId()).orElse(null);
        assertThat( found ).isEqualTo(test1);

        // test that the product doesn't exist
        Optional<Product> fromDb = repository.findById(-100L);
        assertThat(fromDb).isEmpty();
    }

    @Test
    void getAllProductsTest() {

        entityManager.persist(user);
        entityManager.persist(test1);
        entityManager.persist(test2);
        entityManager.persist(test3);
        entityManager.flush();
 

         List<Product> allProducts= repository.findAll();
          

        assertThat(allProducts)
            .hasSize(3)
            .extracting(Product::getProductId, Product::getProductName)
            .containsOnly(
                tuple(test1.getProductId(), test1.getProductName()),
                tuple(test2.getProductId(), test2.getProductName()),
                tuple(test3.getProductId(), test3.getProductName())
            );
    }

    @Test
    void findByProductNameIgnoreCaseContaining() {

        List<Product> allProducts= repository.findByProductNameIgnoreCaseContaining("Am");

        assertThat(allProducts)
            .hasSize(2)
            .extracting(Product::getProductId, Product::getProductName)
            .containsOnly(
                tuple(test1.getProductId(), test1.getProductName()),
                tuple(test3.getProductId(), test3.getProductName())
            );
    }
    

    @Test
    void findByCategoryContains() {

        List<Product> products = repository.findByCategoryContains("USB");

        assertThat(products)
            .hasSize(2)
            .extracting(Product::getProductId, Product::getProductName)
            .containsOnly(
                tuple(test1.getProductId(), test1.getProductName()),
                tuple(test3.getProductId(), test3.getProductName())
            );

    }

    @Test
    void findByProductNameContainsAndCategoryContains(){

        List<Product> products = repository.findByProductNameContainsAndCategoryContains("Amogi", "USB");

        assertThat(products)
            .hasSize(1)
            .extracting(Product::getProductId, Product::getProductName)
            .containsOnly(
                tuple(test1.getProductId(), test1.getProductName())
            );


    }



}
