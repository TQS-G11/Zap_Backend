package tqs.g11.zap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Setter
    @Getter
    Long id;

    @Setter
    @Getter
    @ManyToOne
    User user;
}
