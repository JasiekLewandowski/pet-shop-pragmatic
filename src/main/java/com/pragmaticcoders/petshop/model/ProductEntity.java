package com.pragmaticcoders.petshop.model;

import com.pragmaticcoders.petshop.enums.DiscountType;
import com.pragmaticcoders.petshop.enums.PetType;
import com.pragmaticcoders.petshop.enums.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.pragmaticcoders.petshop.validation.ValidationConstants.BARCODE_LENGTH;
import static com.pragmaticcoders.petshop.validation.ValidationConstants.PRODUCT_NAME_LENGTH;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class ProductEntity {

    @Id
    @Column(length = BARCODE_LENGTH, nullable = false, unique = true)
    private String barcode;

    @Column(length = PRODUCT_NAME_LENGTH, nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false)
    private ProductCategory productCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type", nullable = false)
    private PetType petType;

    @NotNull
    @Column(name = "normal_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal normalPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private DiscountType discountType;

}
