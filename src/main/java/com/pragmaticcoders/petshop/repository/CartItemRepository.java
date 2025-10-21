package com.pragmaticcoders.petshop.repository;

import com.pragmaticcoders.petshop.model.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    List<CartItemEntity> findAllByCartId(java.util.UUID cartId);

    boolean existsByCartIdAndProduct_Barcode(java.util.UUID cartId, String barcode);

    java.util.Optional<CartItemEntity> findByCartIdAndProduct_Barcode(java.util.UUID cartId, String barcode);
}
