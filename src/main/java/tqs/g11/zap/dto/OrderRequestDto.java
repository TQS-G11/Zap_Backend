package tqs.g11.zap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @Getter
    @Setter
    private String buyer;

    @Getter
    @Setter
    private String destination;

    @Getter
    @Setter
    private String notes;

    @Getter
    @Setter
    private String origin;

    @Getter
    @Setter
    private Double storeLat;

    @Getter
    @Setter
    private Double storeLon;
}
