package tqs.g11.zap.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.g11.zap.data.Product;
import tqs.g11.zap.repository.ProductRepository;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository repository;


    @Test
    void getProductByIdTest() {

        Product test = new Product(1l, "Amogi Pen", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 4, 1, 15.5);
        entityManager.persistAndFlush(test); //ensure data is persisted at this point

        // test if product exists
        Product found = repository.findById(test.getId()).orElse(null);
        assertThat( found ).isEqualTo(test);

        // test that the product doesn't exist
        Optional<Product> fromDb = repository.findById(-100L);
        assertThat(fromDb).isEmpty();
    }

    @Test
    void getAllProductsTest() {
        Product test1 = new Product(1l, "Amogi Pen", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 4, 1, 15.5);
        Product test2 = new Product(2l, "USB Cable", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 3, 5, 16.5);
        Product test3 = new Product(3l, "Charger 3", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 7, 10, 7.5);
        

        entityManager.persist(test1);
        entityManager.persist(test2);
        entityManager.persist(test3);
        entityManager.flush();

        List<Product> allProducts= repository.findAll();
          

        assertThat(allProducts)
            .hasSize(3)
            .extracting(Product::getId, Product::getName)
            .containsOnly(
                tuple(test1.getId(), test1.getName()),
                tuple(test2.getId(), test2.getName()),
                tuple(test3.getId(), test3.getName())
            );
    }

    @Test
    void findProductsContainingName(){
        Product test1 = new Product(1l, "Amogi Pen", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 4, 1, 15.5);
        Product test2 = new Product(2l, "USB Cable", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 3, 5, 16.5);
        Product test3 = new Product(3l, "AmogusPen", "https://mir-s3-cdn-cf.behance.net/project_modules/2800_opt_1/7dea57109222637.5fcf37f1395c7.png", "", 7, 10, 7.5);
  
        entityManager.persist(test1);
        entityManager.persist(test2);
        entityManager.persist(test3);
        entityManager.flush();

        List<Product> allProducts= repository.findByNameProductsContains("Am");

        assertThat(allProducts)
            .hasSize(2)
            .extracting(Product::getId, Product::getName)
            .containsOnly(
                tuple(test1.getId(), test1.getName()),
                tuple(test3.getId(), test3.getName())
            );
    }
    

}
