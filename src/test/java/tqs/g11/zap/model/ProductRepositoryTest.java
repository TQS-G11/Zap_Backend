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

        //Product test1 = new Product("Amogi Pen", "", 4);
        Product test1 = new Product("AAA");
        repository.save(test1);

        // test if product exists
        Product found = repository.findById(test1.getProductId()).orElse(null);
        assertThat( found ).isEqualTo(test1);

        // test that the product doesn't exist
        Optional<Product> fromDb = repository.findById(-100L);
        assertThat(fromDb).isEmpty();

        entityManager.clear();
    }

    // @Test
    // void getAllProductsTest() {

    //     Product test1 = new Product("Amogi Pen", "", 4);
    //     Product test2 = new Product("AmogusPen", "", 3);
    //     Product test3 = new Product("Charger 3", "", 7);

    //     entityManager.persist(test1);
    //     entityManager.persist(test2);
    //     entityManager.persist(test3);
    //     entityManager.flush();
 

    //     List<Product> allProducts= repository.findAll();
          

    //     assertThat(allProducts)
    //         .hasSize(3)
    //         .extracting(Product::getId, Product::getName)
    //         .containsOnly(
    //             tuple(test1.getId(), test1.getName()),
    //             tuple(test2.getId(), test2.getName()),
    //             tuple(test3.getId(), test3.getName())
    //         );

    //     entityManager.clear();
    // }

    // @Test
    // void findProductsContainingName(){

    //     Product test1 = new Product("Amogi Pen", "", 4);
    //     Product test2 = new Product("AmogusPen", "", 3);
    //     Product test3 = new Product("Charger 3", "", 7);

    //     entityManager.persist(test1);
    //     entityManager.persist(test2);
    //     entityManager.persist(test3);
    //     entityManager.flush();


    //     List<Product> allProducts= repository.findByNameIgnoreCaseContaining("Am");

    //     assertThat(allProducts)
    //         .hasSize(2)
    //         .extracting(Product::getId, Product::getName)
    //         .containsOnly(
    //             tuple(test1.getId(), test1.getName()),
    //             tuple(test3.getId(), test3.getName())
    //         );

    //     entityManager.clear();
    // }
    

}
