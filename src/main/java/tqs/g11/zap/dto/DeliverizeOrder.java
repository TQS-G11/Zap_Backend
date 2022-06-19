package tqs.g11.zap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class DeliverizeOrder {

    @Getter
    @Setter
    private String destination;

    @Getter
    @Setter
    private String notes;

    @Getter
    @Setter
    private String deliveryStatus;

    @Getter
    @Setter
    private String origin;

    @Getter
    @Setter
    private Double price;

    @Getter
    @Setter
    private String requestedAt;

    @Getter
    @Setter
    private String acceptedAt;

}
