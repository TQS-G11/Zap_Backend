package tqs.g11.zap.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.g11.zap.repository.ProductRepository;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository repository;


    @Test
    void getProductByIdTest() {

        Product test1 = new Product("Amogi Pen", "", 4);
        entityManager.persistAndFlush(test1);

        // test if product exists
        Product found = repository.findById(test1.getProductId()).orElse(null);
        assertThat( found ).isEqualTo(test1);

        // test that the product doesn't exist
        Optional<Product> fromDb = repository.findById(-100L);
        assertThat(fromDb).isEmpty();

        entityManager.clear();
    }

    @Test
    void getAllProductsTest() {

         Product test1 = new Product("Amogi Pen", "", 4);
         Product test2 = new Product("AmogusPen", "", 3);
         Product test3 = new Product("Charger 3", "", 7);

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

        entityManager.clear();
    }

    @Test
    void findProductsContainingName(){

        Product test1 = new Product("Amogi Pen", "", 4);
        Product test2 = new Product("Charger 3", "", 7);
        Product test3 = new Product("AmogusPen", "", 3);

        entityManager.persist(test1);
        entityManager.persist(test2);
        entityManager.persist(test3);
        entityManager.flush();


        List<Product> allProducts= repository.findByProductNameIgnoreCaseContaining("Am");

        assertThat(allProducts)
            .hasSize(2)
            .extracting(Product::getProductId, Product::getProductName)
            .containsOnly(
                tuple(test1.getProductId(), test1.getProductName()),
                tuple(test3.getProductId(), test3.getProductName())
            );

        entityManager.clear();
    }
    

}
