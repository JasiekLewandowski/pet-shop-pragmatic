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
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal cartTotal = BigDecimal.ZERO;
    private BigDecimal cartTotalWithDiscount = BigDecimal.ZERO;

}
