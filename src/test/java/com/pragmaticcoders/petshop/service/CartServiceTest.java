package com.pragmaticcoders.petshop.service;

import com.pragmaticcoders.petshop.dto.CartDTO;
import com.pragmaticcoders.petshop.enums.DiscountType;
import com.pragmaticcoders.petshop.mapper.CartDTOMapper;
import com.pragmaticcoders.petshop.model.CartEntity;
import com.pragmaticcoders.petshop.model.CartItemEntity;
import com.pragmaticcoders.petshop.model.ProductEntity;
import com.pragmaticcoders.petshop.repository.CartRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartDTOMapper cartDTOMapper;

    @Mock
    private CartCalculationService cartCalculationService;

    @InjectMocks
    private CartService cartService;

    private final String SESSION_ID = "test-session";

    @BeforeAll
    static void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
    }

    @Test
    void getCartWhenOk() {
        // GIVEN
        var cart = new CartEntity();
        cart.setSessionId(SESSION_ID);

        var product = new ProductEntity();
        product.setBarcode("123");
        product.setNormalPrice(BigDecimal.valueOf(10));
        product.setDiscountType(DiscountType.NONE);

        var cartItem = new CartItemEntity();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.setCartItems(List.of(cartItem));

        // WHEN
        when(cartRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.of(cart));
        when(cartCalculationService.calculateCartTotal(any())).thenReturn(BigDecimal.valueOf(20));
        when(cartCalculationService.calculateCartTotalWithoutPromotions(any())).thenReturn(BigDecimal.valueOf(20));
        when(cartDTOMapper.toCartDTO(cart)).thenReturn(new CartDTO());

        var result = cartService.getCart(SESSION_ID);

        // THEN
        assertEquals(BigDecimal.valueOf(20), result.getCartTotal());
        assertEquals(BigDecimal.valueOf(20), result.getCartTotalWithDiscount());
    }

    @Test
    void getCartWhenNoCart() {
        // GIVEN
        var cart = new CartEntity();
        cart.setSessionId(SESSION_ID);

        var cartDTO = new CartDTO();
        cartDTO.setCartTotal(BigDecimal.ZERO);
        cartDTO.setCartTotalWithDiscount(BigDecimal.ZERO);

        // WHEN
        when(cartRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.empty());
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cart);
        when(cartDTOMapper.toCartDTO(any(CartEntity.class))).thenReturn(cartDTO);

        var result = cartService.getCart(SESSION_ID);

        // THEN
        assertEquals(BigDecimal.ZERO, result.getCartTotal());
        assertEquals(BigDecimal.ZERO, result.getCartTotalWithDiscount());
        verify(cartCalculationService, never()).calculateCartTotal(any());
        verify(cartCalculationService, never()).calculateCartTotalWithoutPromotions(any());
        verify(cartRepository).save(any(CartEntity.class));
    }

}