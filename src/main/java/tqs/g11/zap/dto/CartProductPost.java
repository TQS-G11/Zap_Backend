package tqs.g11.zap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
public class CartProductPost {
    @Getter
    @Setter
    private Long productId;

    @Getter
    @Setter
    private Integer quantity;
}
