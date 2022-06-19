package tqs.g11.zap.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tqs.g11.zap.client.TqsBasicHttpClient;
import tqs.g11.zap.enums.UserRoles;
import tqs.g11.zap.model.CartProduct;
import tqs.g11.zap.model.Product;
import tqs.g11.zap.model.User;
import tqs.g11.zap.repository.CartRepository;

@ExtendWith(MockitoExtension.class)
class ServiceWithMockDeliverize {
    
    @Mock(lenient = true)
    private CartRepository cartRepository;

    @Mock
    private TqsBasicHttpClient client;

    private CartService service;

    private String url = "api/zap";


    @BeforeEach
    void setUp() {

        User user1 = new User("user1", "Caio Costela", "amogus123", UserRoles.MANAGER);
        Product p1 = new Product(1L, "Among Us Pen Drive", "url1", "An Among Us pen drive", 69, user1, 420.69, "Pen Drive");


        CartProduct cp1 = new CartProduct(1L, p1, 1, user1);

        List<CartProduct> cartList = new ArrayList<>(Arrays.asList(cp1));

        when(cartRepository.findByUserId(1L)).thenReturn(cartList);
    }
}
