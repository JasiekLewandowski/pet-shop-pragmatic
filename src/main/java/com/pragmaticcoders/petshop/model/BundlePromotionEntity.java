package com.pragmaticcoders.petshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bundle_promotion")
public class BundlePromotionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String productBarcodeA;

    @NotBlank
    private String productBarcodeB;

    @NotNull
    private BigDecimal bundlePrice;

    @NotNull
    private boolean active;

}
