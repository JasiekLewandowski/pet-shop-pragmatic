package com.pragmaticcoders.petshop.repository;

import com.pragmaticcoders.petshop.enums.DiscountType;
import com.pragmaticcoders.petshop.enums.PetType;
import com.pragmaticcoders.petshop.enums.ProductCategory;
import com.pragmaticcoders.petshop.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {


    List<ProductEntity> findByPetType(PetType petType);

    List<ProductEntity> findByProductCategory(ProductCategory category);

    List<ProductEntity> findByDiscountType(DiscountType discountType);

    Optional<ProductEntity> findByBarcode(String barcode);

}
