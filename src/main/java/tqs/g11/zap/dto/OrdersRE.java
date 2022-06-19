package tqs.g11.zap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.g11.zap.model.Order;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class OrdersRE {

    @Getter
    @Setter
    private List<String> errors = new ArrayList<>();

    @Getter
    @Setter
    private List<OrderRE> orders;

    public void addError(String error) {
        errors.add(error);
    }

    public void addOrder(OrderRE order) {
        orders.add(order);
    }
}
