package com.pragmaticcoders.petshop.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String barcode) {
        super("Product with barcode " + barcode + " not found");
    }
}