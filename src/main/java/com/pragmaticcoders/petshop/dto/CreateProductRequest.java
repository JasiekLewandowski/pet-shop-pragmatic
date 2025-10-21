package com.pragmaticcoders.petshop.dto;

import com.pragmaticcoders.petshop.enums.DiscountType;
import com.pragmaticcoders.petshop.enums.PetType;
import com.pragmaticcoders.petshop.enums.ProductCategory;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

import static com.pragmaticcoders.petshop.validation.ValidationConstants.*;
import static com.pragmaticcoders.petshop.validation.ValidationMessages.*;

@Data
public class CreateProductRequest {

    @NotBlank
    @Pattern(regexp = "\\d{13}", message = BARCODE_WRONG_FORMAT)
    private String barcode;

    @NotBlank
    @Size(max = PRODUCT_NAME_LENGTH, message = PRODUCT_NAME_TOO_LONG)
    private String name;

    @NotNull
    private ProductCategory productCategory;

    @NotNull
    private PetType petType;

    @NotNull
    @DecimalMin(value = MIN_PRICE, message = PRICE_TOO_SMALL)
    @DecimalMax(value = MAX_PRICE, message = PRICE_TOO_BIG)
    private BigDecimal normalPrice;

    @NotNull
    private DiscountType discountType;

    @Min(0)
    @Max(PRODUCT_QUANTITY_MAX)
    private int requiredQuantity;

    @NotNull
    @DecimalMin(value = MIN_PRICE, message = PRICE_TOO_SMALL)
    @DecimalMax(value = MAX_PRICE, message = PRICE_TOO_BIG)
    private BigDecimal specialPrice;
}