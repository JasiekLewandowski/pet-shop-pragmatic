package com.pragmaticcoders.petshop.service;


import com.pragmaticcoders.petshop.dto.CartDTO;
import com.pragmaticcoders.petshop.dto.CreateCartItemRequest;
import com.pragmaticcoders.petshop.mapper.CartDTOMapper;
import com.pragmaticcoders.petshop.model.CartEntity;
import com.pragmaticcoders.petshop.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartCalculationService cartCalculationService;

    @Autowired
    private CartDTOMapper cartDTOMapper;

    public CartDTO getCart(String sessionId) {
        var cartEntity = getOrCreateCart(sessionId);
        var cartDTO = cartDTOMapper.toCartDTO(cartEntity);

        if (!cartDTO.getItems().isEmpty()) {
            cartDTO.setCartTotal(calculateCartTotal(cartEntity));
            cartDTO.setCartTotalWithDiscount(calculateCartTotalWithDiscount(cartEntity));
        }
        return cartDTO;
    }

    public CartEntity getOrCreateCart(String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    var newCart = new CartEntity();
                    newCart.setSessionId(sessionId);
                    return cartRepository.save(newCart);
                });
    }

    public CartDTO addCartItem(CreateCartItemRequest request, String sessionId) {
        var cartEntity = getOrCreateCart(sessionId);

        var cartItemEntity = cartItemService.addCartItem(cartEntity, request.barcode(), request.quantity());
        cartEntity.getCartItems().add(cartItemEntity);
        cartEntity.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cartEntity);

        var cartDTO = cartDTOMapper.toCartDTO(cartEntity);
        cartDTO.setCartTotal(calculateCartTotalWithoutPromotions(cartEntity));

        return cartDTO;
    }

    private BigDecimal calculateCartTotalWithoutPromotions(CartEntity cartEntity) {
        return cartCalculationService.calculateCartTotalWithoutPromotions(cartEntity);
    }

    private BigDecimal calculateCartTotal(CartEntity cartEntity) {
        return cartCalculationService.calculateCartTotal(cartEntity);
    }

    private BigDecimal calculateCartTotalWithDiscount(CartEntity cartEntity) {
        return cartCalculationService.calculateCartTotalWithoutPromotions(cartEntity);
    }

}
