package com.pragmaticcoders.petshop.service;

import com.pragmaticcoders.petshop.enums.DiscountType;
import com.pragmaticcoders.petshop.model.CartEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartCalculationService {

    @Autowired
    private MultipackPromotionService multipackPromotionService;

    @Autowired
    private BundlePromotionService bundlePromotionService;


    public BigDecimal calculateCartTotal(CartEntity cartEntity) {
        return cartEntity.getCartItems().stream()
                .map(cartItem ->
                        calculateQuantityByPrice(cartItem.getProduct().getNormalPrice(), cartItem.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateCartTotalWithDiscount(CartEntity cartEntity) {
        var nonDiscountedProductsTotal = calculateNonDiscountedProducts(cartEntity);
        var bundleItemsTotal = bundlePromotionService.getDiscountedItemsTotal(cartEntity);
        var multipackItemsTotal = multipackPromotionService.getDiscountedItemsTotal(cartEntity);

        return nonDiscountedProductsTotal
                .add(multipackItemsTotal)
                .add(bundleItemsTotal);
    }

    private BigDecimal calculateNonDiscountedProducts(CartEntity cartEntity) {
        return cartEntity.getCartItems().stream()
                .filter(item -> item.getProduct().getDiscountType() == DiscountType.NONE)
                .map(item -> item.getProduct().getNormalPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateQuantityByPrice(BigDecimal price, int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));

    }

}
