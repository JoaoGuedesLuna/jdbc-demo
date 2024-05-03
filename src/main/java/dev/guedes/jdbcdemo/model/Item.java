package dev.guedes.jdbcdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * @author João Guedes
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Item {

    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private Product product;
    private Order order;

}
