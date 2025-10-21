package com.pragmaticcoders.petshop.repository;

import com.pragmaticcoders.petshop.model.MultipackPromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MultipackPromotionRepository extends JpaRepository<MultipackPromotionEntity, Long> {

    Optional<MultipackPromotionEntity> findByProductBarcodeAndActiveTrue(String productBarcode);

}
