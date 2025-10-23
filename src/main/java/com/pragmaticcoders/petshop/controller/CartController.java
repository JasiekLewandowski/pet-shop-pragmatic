package com.pragmaticcoders.petshop.controller;

import com.pragmaticcoders.petshop.dto.CartDTO;
import com.pragmaticcoders.petshop.dto.CreateCartItemRequest;
import com.pragmaticcoders.petshop.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestHeader("X-Session-Id") @NotBlank String xSessionId) {
        return ResponseEntity.ok(cartService.getCart(xSessionId));
    }

    @PostMapping("/item")
    public ResponseEntity<CartDTO> addCartItem(@RequestHeader("X-Session-Id") @NotBlank String xSessionId,
                                               @Valid @RequestBody CreateCartItemRequest request) {
        return ResponseEntity.ok(cartService.addCartItem(request, xSessionId));
    }


}
