package com.pragmaticcoders.petshop.service;

import com.pragmaticcoders.petshop.model.CartEntity;
import com.pragmaticcoders.petshop.model.CartItemEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartItemService {

    @Autowired
    private ProductService productService;

    public CartEntity addCartItem(CartEntity cartEntity, String barcode, int quantity) {
        Optional<CartItemEntity> cartItemOpt = findItemByBarcode(cartEntity, barcode);

        if (cartItemOpt.isPresent()) {
            var cartItem = cartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            createNewCartItem(cartEntity, barcode, quantity);
        }

        return cartEntity;
    }

    private Optional<CartItemEntity> findItemByBarcode(CartEntity cartEntity, String barcode) {
        return cartEntity.getCartItems().stream()
                .filter(item -> item.getProduct().getBarcode().equals(barcode))
                .findFirst();
    }

    private CartItemEntity updateCartItemQuantity(CartItemEntity cartItem, int quantity) {
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        return cartItem;
    }

    private CartItemEntity createNewCartItem(CartEntity cartEntity, String barcode, int quantity) {
        var product = productService.getProductByBarcode(barcode);

        var cartItem = new CartItemEntity()
                .setCart(cartEntity)
                .setProduct(product)
                .setQuantity(quantity)
                .setPriceStamp(product.getNormalPrice());

        cartEntity.getCartItems().add(cartItem);

        return cartItem;
    }

}
