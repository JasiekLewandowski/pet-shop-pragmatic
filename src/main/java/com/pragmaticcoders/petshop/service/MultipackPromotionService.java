package com.pragmaticcoders.petshop.service;

import com.pragmaticcoders.petshop.enums.DiscountType;
import com.pragmaticcoders.petshop.model.CartEntity;
import com.pragmaticcoders.petshop.model.CartItemEntity;
import com.pragmaticcoders.petshop.model.MultipackPromotionEntity;
import com.pragmaticcoders.petshop.repository.MultipackPromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MultipackPromotionService {

    @Autowired
    private MultipackPromotionRepository multipackPromotionRepository;

    public BigDecimal getDiscountedItemsTotal(CartEntity cartEntity) {
        var total = BigDecimal.ZERO;

        var multiPackItemsInCart = getCartItemsByDiscountType(cartEntity, DiscountType.MULTIPACK);

        for (CartItemEntity item : multiPackItemsInCart) {
            var promo = findPromotionForMultipackProduct(item.getProduct().getBarcode());
            if (promo.isPresent()) {
                total = total.add(calculateMultiPackItemsTotal(item, promo.get()));
            } else {
                total = total.add(calculateQuantityByPrice(item.getProduct().getNormalPrice(), item.getQuantity()));
            }
        }

        return total;
    }

    private BigDecimal calculateMultiPackItemsTotal(CartItemEntity cartItem, MultipackPromotionEntity promo) {
        int multipackItemsQuantity = cartItem.getQuantity() / promo.getRequiredQuantity();
        int remaindersQuantity = cartItem.getQuantity() % promo.getRequiredQuantity();

        var normalPrice = cartItem.getProduct().getNormalPrice();
        var promoPrice = promo.getSpecialPrice();

        var totalForMultipacks = calculateQuantityByPrice(promoPrice, multipackItemsQuantity);
        var totalForRemainders = calculateQuantityByPrice(normalPrice, remaindersQuantity);

        return totalForMultipacks.add(totalForRemainders);
    }

    private List<CartItemEntity> getCartItemsByDiscountType(CartEntity cart, DiscountType discountType) {
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getDiscountType() == discountType)
                .toList();
    }

    private Optional<MultipackPromotionEntity> findPromotionForMultipackProduct(String barcode) {
        return multipackPromotionRepository.findByProductBarcodeAndActiveTrue(barcode);
    }

    private BigDecimal calculateQuantityByPrice(BigDecimal price, int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));

    }

}
