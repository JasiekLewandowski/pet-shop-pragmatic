package com.pragmaticcoders.petshop.repository;

import com.pragmaticcoders.petshop.model.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {

    Optional<CartEntity> findBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);

}
