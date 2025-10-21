package com.pragmaticcoders.petshop.service;

import com.pragmaticcoders.petshop.enums.DiscountType;
import com.pragmaticcoders.petshop.model.BundlePromotionEntity;
import com.pragmaticcoders.petshop.model.CartEntity;
import com.pragmaticcoders.petshop.model.CartItemEntity;
import com.pragmaticcoders.petshop.repository.BundlePromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BundlePromotionService {

    @Autowired
    private BundlePromotionRepository bundlePromotionRepository;

    public BigDecimal getDiscountedItemsTotal(CartEntity cart) {
        var discountedItemsTotal = BigDecimal.ZERO;

        List<CartItemEntity> bundleItemsInCart = getCartItemsByDiscountType(cart, DiscountType.BUNDLE);

        List<BundlePromotionEntity> activeBundles = bundlePromotionRepository.findByActiveTrue();

        for (BundlePromotionEntity bundle : activeBundles) {
            var cartItemA = checkForBundleItemInCart(cart, bundle.getProductBarcodeA());
            var cartItemB = checkForBundleItemInCart(cart, bundle.getProductBarcodeB());

            if (cartItemA != null && cartItemB != null) {
                int bundleCount = Math.min(cartItemA.getQuantity(), cartItemB.getQuantity());

                discountedItemsTotal = discountedItemsTotal.add(bundle.getBundlePrice().multiply(BigDecimal.valueOf(bundleCount)));

                cartItemA.setQuantity(cartItemA.getQuantity() - bundleCount);
                cartItemB.setQuantity(cartItemB.getQuantity() - bundleCount);
            }
        }

        var remainingItemsTotal = calculateTotalForRemainingItems(bundleItemsInCart);

        return discountedItemsTotal.add(remainingItemsTotal);
    }

    private BigDecimal calculateTotalForRemainingItems(List<CartItemEntity> cartItems) {
        return cartItems.stream()
                .map(item -> item.getProduct().getNormalPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CartItemEntity checkForBundleItemInCart(CartEntity cart, String barcode) {
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getBarcode().equals(barcode))
                .findFirst()
                .orElse(null);
    }

    private List<CartItemEntity> getCartItemsByDiscountType(CartEntity cart, DiscountType discountType) {
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getDiscountType() == discountType)
                .toList();
    }

}
