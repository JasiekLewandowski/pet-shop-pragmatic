package com.pragmaticcoders.petshop.controller;

import com.pragmaticcoders.petshop.dto.CartDTO;
import com.pragmaticcoders.petshop.dto.CreateCartItemRequest;
import com.pragmaticcoders.petshop.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartDTO> getCart(@RequestParam @NotBlank String sessionId) {
        return ResponseEntity.ok(cartService.getCart(sessionId));
    }

    @PostMapping("/item")
    public ResponseEntity<CartDTO> addCartItem(@RequestParam @NotBlank String sessionId,
                                               @Valid @RequestBody CreateCartItemRequest request) {
        return ResponseEntity.ok(cartService.addCartItem(request, sessionId));
    }


}
