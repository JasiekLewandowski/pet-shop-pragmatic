package com.pragmaticcoders.petshop.service;

import com.pragmaticcoders.petshop.dto.CartDTO;
import com.pragmaticcoders.petshop.dto.CartItemDTO;
import com.pragmaticcoders.petshop.mapper.CartDTOMapper;
import com.pragmaticcoders.petshop.model.CartEntity;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
    void shouldGetCart_WhenCartExistsAndIsNotEmpty() {
        // GIVEN
        var resultTotal = BigDecimal.valueOf(20);

        var cart = new CartEntity();
        cart.setSessionId(SESSION_ID);

        var cartItem = new CartItemDTO();

        var resultCartDTO = CartDTO.builder()
                .sessionId(SESSION_ID)
                .cartTotal(resultTotal)
                .cartTotalWithDiscount(resultTotal)
                .items(List.of(cartItem))
                .build();

        // WHEN
        when(cartRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.of(cart));
        when(cartCalculationService.calculateCartTotal(any())).thenReturn(resultTotal);
        when(cartCalculationService.calculateCartTotalWithoutPromotions(any())).thenReturn(resultTotal);
        when(cartDTOMapper.toCartDTO(cart)).thenReturn(resultCartDTO);

        var result = cartService.getCart(SESSION_ID);

        // THEN
        assertEquals(resultTotal, result.getCartTotal());
        assertEquals(resultTotal, result.getCartTotalWithDiscount());
        assertEquals(SESSION_ID, result.getSessionId());
        assertThat(result.getItems()).isNotEmpty();

        verify(cartRepository, never()).save(any());
        verify(cartDTOMapper).toCartDTO(any(CartEntity.class));

        verify(cartCalculationService).calculateCartTotal(any(CartEntity.class));
        verify(cartCalculationService).calculateCartTotalWithoutPromotions(any(CartEntity.class));
    }

    @Test
    void shouldGetCart_WhenCartExistsAndIsEmpty() {
        // GIVEN
        var resultTotal = BigDecimal.ZERO;

        var cart = new CartEntity();
        cart.setSessionId(SESSION_ID);

        var resultCartDTO = CartDTO.builder()
                .sessionId(SESSION_ID)
                .cartTotal(resultTotal)
                .cartTotalWithDiscount(resultTotal)
                .items(List.of())
                .build();

        // WHEN
        when(cartRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.of(cart));
        when(cartDTOMapper.toCartDTO(cart)).thenReturn(resultCartDTO);

        var result = cartService.getCart(SESSION_ID);

        // THEN
        assertEquals(resultTotal, result.getCartTotal());
        assertEquals(resultTotal, result.getCartTotalWithDiscount());
        assertEquals(SESSION_ID, result.getSessionId());
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getItems()).isNotNull();

        verify(cartRepository, never()).save(any());
        verify(cartDTOMapper).toCartDTO(any(CartEntity.class));

        verify(cartCalculationService, never()).calculateCartTotal(any());
        verify(cartCalculationService, never()).calculateCartTotalWithoutPromotions(any());
    }

    @Test
    void shouldCreateCart_WhenCartDoesNotExist() {
        // GIVEN
        var createdCart = CartEntity.builder()
                .id(UUID.randomUUID())
                .sessionId(SESSION_ID)
                .build();

        var resultCartDTO = CartDTO.builder()
                .sessionId(SESSION_ID)
                .cartTotal(BigDecimal.ZERO)
                .cartTotalWithDiscount(BigDecimal.ZERO)
                .items(List.of())
                .build();

        // WHEN
        when(cartRepository.findBySessionId(SESSION_ID)).thenReturn(Optional.empty());
        when(cartRepository.save(any(CartEntity.class))).thenReturn(createdCart);
        when(cartDTOMapper.toCartDTO(createdCart)).thenReturn(resultCartDTO);

        var result = cartService.getCart(SESSION_ID);

        // THEN
        assertEquals(BigDecimal.ZERO, result.getCartTotal());
        assertEquals(BigDecimal.ZERO, result.getCartTotalWithDiscount());
        assertEquals(SESSION_ID, result.getSessionId());
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getItems()).isNotNull();

        verify(cartRepository).save(any(CartEntity.class));
        verify(cartDTOMapper).toCartDTO(any(CartEntity.class));


        verify(cartCalculationService, never()).calculateCartTotal(any());
        verify(cartCalculationService, never()).calculateCartTotalWithoutPromotions(any());
    }

}