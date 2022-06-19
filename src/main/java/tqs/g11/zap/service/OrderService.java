package tqs.g11.zap.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import tqs.g11.zap.client.TqsBasicHttpClient;
import tqs.g11.zap.dto.LoginUser;
import tqs.g11.zap.dto.DeliverizeOrder;
import tqs.g11.zap.dto.OrderRE;
import tqs.g11.zap.dto.OrdersRE;
import tqs.g11.zap.model.User;

import java.io.IOException;

@Service
public class OrderService {
    private final UsersService usersService;

    private static final String deliverizeBaseURI = "http://deliverizebackend:8080";
    private static final String ordersByBuyer = deliverizeBaseURI + "/api/deliveries/company/buyer";
    private static final String ordersURI = deliverizeBaseURI + "/api/deliveries";
    private static final String ordersCompanyURI = deliverizeBaseURI + "/api/deliveries/company";

    private static final String deliverizeLogin = deliverizeBaseURI + "/api/users/login";
    private static final String storeUsername = "Zap";
    private static final String storePassword = "zapogus123";


    public OrderService(UsersService usersService) {
        this.usersService = usersService;
    }

    public ResponseEntity<OrdersRE> getAllOrders(Authentication auth) throws IOException {
        return getOrders(auth, false);
    }

    public ResponseEntity<OrdersRE> getOrderByClient(Authentication auth) throws IOException {
        return getOrders(auth, true);
    }

    public ResponseEntity<OrderRE> getOrderById(Long id) throws IOException {
        OrderRE re = new OrderRE();
        TqsBasicHttpClient httpClient = new TqsBasicHttpClient();

        String token = getCompanyToken(httpClient);

        JsonObject response = httpClient.doHttpGet(ordersURI + "/" + id, token);
        JsonArray errors = response.getAsJsonArray("errors");

        if (re.getErrors().isEmpty()) {
            if (errors.size() == 0) {
                JsonElement orderJson = response.get("order");
                DeliverizeOrder order = createDeliverizeOrder(orderJson);
                re.setOrder(order);
                return ResponseEntity.status(HttpStatus.OK).body(re);
            }
            else {
                for (JsonElement errorJson : errors) {
                    String error = errorJson.getAsString();
                    re.addError(error);
                }
            }
        }

        return ResponseEntity.badRequest().body(re);
    }
    private ResponseEntity<OrdersRE> getOrders(Authentication auth, boolean hasBuyer) throws IOException {
        OrdersRE re = new OrdersRE();
        TqsBasicHttpClient httpClient = new TqsBasicHttpClient();
        User client = usersService.getAuthUser(auth);

        String token = getCompanyToken(httpClient);

        JsonObject response;

        if (hasBuyer)  {
            response = httpClient.doHttpGet(ordersByBuyer + "/" + client.getUsername(), token);
        }
        else {
            response = httpClient.doHttpGet(ordersCompanyURI, token);
        }
        JsonArray errors = response.getAsJsonArray("errors");
        if (errors.size() == 0) {
            JsonArray orders = response.getAsJsonArray("orders");
            for (JsonElement jsonElement : orders) {
                DeliverizeOrder order = createDeliverizeOrder(jsonElement);
                re.addOrder(order);
            }
            return ResponseEntity.status(HttpStatus.OK).body(re);
        }
        else {
            for (JsonElement errorJson : errors) {
                String error = errorJson.getAsString();
                re.addError(error);
            }
        }

        return ResponseEntity.badRequest().body(re);
    }

    private DeliverizeOrder createDeliverizeOrder(JsonElement jsonElement) {
        JsonObject jsonOrder = jsonElement.getAsJsonObject();
        String accepted;

        try {
            accepted = jsonOrder.get("acceptedAt").getAsString();
        }
        catch (UnsupportedOperationException e) {
            accepted = null;
        }
        return new DeliverizeOrder(
                jsonOrder.get("destination").getAsString(),
                jsonOrder.get("notes").getAsString(),
                jsonOrder.get("deliveryStatus").getAsString(),
                jsonOrder.get("origin").getAsString(),
                jsonOrder.get("price").getAsDouble(),
                jsonOrder.get("requestedAt").getAsString(),
                accepted
        );
    }

    private String getCompanyToken(TqsBasicHttpClient client) throws IOException {
        LoginUser loginUser = new LoginUser(storeUsername, storePassword);
        JsonObject loginResponse = client.doHttpPost(deliverizeLogin, loginUser, null);

        return loginResponse.getAsJsonObject("token").get("token").getAsString();
    }
}
