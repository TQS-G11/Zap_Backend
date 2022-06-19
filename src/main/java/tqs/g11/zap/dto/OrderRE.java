package tqs.g11.zap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class OrderRE {

    @Getter
    @Setter
    private List<String> errors = new ArrayList<>();

    @Getter
    @Setter
    private DeliverizeOrder order;

    public void addError(String error) {
        errors.add(error);
    }
}
