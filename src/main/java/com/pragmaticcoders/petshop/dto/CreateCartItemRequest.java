package com.pragmaticcoders.petshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateCartItemRequest(
        @NotBlank String barcode,
        @Min(1) int quantity
) {
}
