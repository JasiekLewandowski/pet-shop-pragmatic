package com.pragmaticcoders.petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private String sessionId;
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal cartTotal;
    private BigDecimal cartTotalWithDiscount;

}
