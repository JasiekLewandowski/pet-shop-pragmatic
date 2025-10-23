package com.pragmaticcoders.petshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private String barcode;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;

}
