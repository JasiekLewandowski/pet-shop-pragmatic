package com.pragmaticcoders.petshop.repository;

import com.pragmaticcoders.petshop.model.BundlePromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BundlePromotionRepository extends JpaRepository<BundlePromotionEntity, Long> {

    List<BundlePromotionEntity> findByActiveTrue();

}
