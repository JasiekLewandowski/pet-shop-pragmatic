package com.pragmaticcoders.petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private String sessionId;
    private List<CartItemDTO> cartItems = new ArrayList<>();
    @Builder.Default
    private BigDecimal cartTotal = new BigDecimal("0.00");
    @Builder.Default
    private BigDecimal cartTotalWithDiscount = new BigDecimal("0.00");

}
