package tqs.g11.zap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class CartCheckoutPostDTO {
    @Getter
    @Setter
    private String destination;

    @Getter
    @Setter
    private String notes;
}
