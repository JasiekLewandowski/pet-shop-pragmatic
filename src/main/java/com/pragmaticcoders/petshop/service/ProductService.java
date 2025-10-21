package com.pragmaticcoders.petshop.service;

import com.pragmaticcoders.petshop.exceptions.ProductNotFoundException;
import com.pragmaticcoders.petshop.model.ProductEntity;
import com.pragmaticcoders.petshop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductEntity getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new ProductNotFoundException(barcode));
    }

}
