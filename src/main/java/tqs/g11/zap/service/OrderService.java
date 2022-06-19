package tqs.g11.zap.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tqs.g11.zap.client.TqsBasicHttpClient;
import tqs.g11.zap.dto.LoginUser;
import tqs.g11.zap.dto.OrderRE;
import tqs.g11.zap.dto.OrdersRE;
import tqs.g11.zap.model.Order;
import tqs.g11.zap.model.User;
import tqs.g11.zap.repository.OrderRepository;

import java.io.IOException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UsersService usersService;

    private static final String deliverizeBaseURI = "http://deliverizebackend:8080";
    private static final String ordersByBuyer = deliverizeBaseURI + "/api/deliveries/company/buyer";
    private static final String ordersURI = deliverizeBaseURI + "/api/deliveries/company";

    private static final String deliverizeLogin = deliverizeBaseURI + "/api/users/login";
    private static final String storeUsername = "Zap";
    private static final String storePassword = "zapogus123";


    public OrderService(OrderRepository orderRepository, UsersService usersService) {
        this.orderRepository = orderRepository;
        this.usersService = usersService;
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public ResponseEntity<OrdersRE> getAllOrders(Authentication auth) throws IOException {
        return getOrders(auth, false);
    }

    public ResponseEntity<OrdersRE> getOrderByClient(Authentication auth) throws IOException {
        return getOrders(auth, true);
    }
    private ResponseEntity<OrdersRE> getOrders(Authentication auth, boolean hasBuyer) throws IOException {
        OrdersRE re = new OrdersRE();
        TqsBasicHttpClient httpClient = new TqsBasicHttpClient();
        User client = usersService.getAuthUser((UserDetails) auth.getPrincipal());

        LoginUser loginUser = new LoginUser(storeUsername, storePassword);
        JsonObject loginResponse = httpClient.doHttpPost(deliverizeLogin, loginUser, null);

        String token = loginResponse.getAsJsonObject("token").get("token").getAsString();

        JsonObject response;

        if (hasBuyer)  {
            response = httpClient.doHttpGet(ordersByBuyer + "/" + client.getUsername(), token);
        }
        else {
            response = httpClient.doHttpGet(ordersURI, token);
        }
        JsonArray errors = response.getAsJsonArray("errors");
        if (errors.size() == 0) {
            JsonArray orders = response.getAsJsonArray("orders");
            for (JsonElement jsonElement : orders) {
                OrderRE order = createOrderRE(jsonElement);
                re.addOrder(order);
            }
            return ResponseEntity.status(HttpStatus.OK).body(re);
        }

        return null;
    }

    private OrderRE createOrderRE(JsonElement jsonElement) {
        JsonObject jsonOrder = jsonElement.getAsJsonObject();
        return new OrderRE(
                jsonOrder.get("destination").getAsString(),
                jsonOrder.get("notes").getAsString(),
                jsonOrder.get("deliveryStatus").getAsString(),
                jsonOrder.get("origin").getAsString(),
                jsonOrder.get("price").getAsDouble(),
                jsonOrder.get("requestedAt").getAsString(),
                jsonOrder.get("acceptedAt").getAsString()
        );
    }
}
