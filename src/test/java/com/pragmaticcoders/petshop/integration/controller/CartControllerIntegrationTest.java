package com.pragmaticcoders.petshop.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragmaticcoders.petshop.dto.CartDTO;
import com.pragmaticcoders.petshop.dto.CreateCartItemRequest;
import com.pragmaticcoders.petshop.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = {"/products_and_promotions.sql", "/carts_and_cart_items.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ActiveProfiles("test")
class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String SESSION_HEADER = "X-Session-Id";

    @Test
    void shouldReturnCartBySessionId_WhenCartExists_NoDiscount() throws Exception {
        // GIVEN
        var sessionId = "existing-cart-session-123";

        var mvcResult = mockMvc.perform(get("/cart")
                        .header(SESSION_HEADER, sessionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var cartDTO = objectMapper.readValue(jsonResponse, CartDTO.class);

        // THEN
        assertThat(cartDTO).isNotNull();
        assertThat(cartDTO.getSessionId()).isEqualTo(sessionId);
        assertThat(cartDTO.getCartTotal()).isEqualTo(new BigDecimal("30.00"));
        assertThat(cartDTO.getCartTotalWithDiscount()).isEqualTo(new BigDecimal("30.00"));

        assertThat(cartDTO.getCartItems()).isNotNull();

        assertThat(cartDTO.getCartItems().size()).isEqualTo(2);

        assertThat(cartDTO.getCartItems().get(0).getBarcode()).isEqualTo("1111111111111");
        assertThat(cartDTO.getCartItems().get(0).getProductName()).isEqualTo("Kocia karma");
        assertThat(cartDTO.getCartItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(cartDTO.getCartItems().get(0).getUnitPrice()).isEqualTo(new BigDecimal("10.00"));

        assertThat(cartDTO.getCartItems().get(1).getBarcode()).isEqualTo("2222222222222");
        assertThat(cartDTO.getCartItems().get(1).getProductName()).isEqualTo("Psia karma");
        assertThat(cartDTO.getCartItems().get(1).getQuantity()).isEqualTo(1);
        assertThat(cartDTO.getCartItems().get(1).getUnitPrice()).isEqualTo(new BigDecimal("10.00"));
    }

    @Test
    void shouldCreateCart() throws Exception {
        // GIVEN
        var sessionId = "not-existing-cart-session-123";

        var mvcResult = mockMvc.perform(get("/cart")
                        .header(SESSION_HEADER, sessionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var cartDTO = objectMapper.readValue(jsonResponse, CartDTO.class);

        // THEN
        assertThat(cartDTO).isNotNull();
        assertThat(cartDTO.getSessionId()).isEqualTo(sessionId);
        assertThat(cartDTO.getCartTotal()).isEqualTo(new BigDecimal("0.00"));
        assertThat(cartDTO.getCartTotalWithDiscount()).isEqualTo(new BigDecimal("0.00"));

        assertThat(cartDTO.getCartItems()).isNotNull();
        assertThat(cartDTO.getCartItems()).isEmpty();

        assertThat(cartRepository.findBySessionId(sessionId)).isPresent();
    }

    @Test
    void shouldIncludeMultipackPromotion() throws Exception {
        // GIVEN
        var sessionId = "multipack-cart-session-123";

        var mvcResult = mockMvc.perform(get("/cart")
                        .header(SESSION_HEADER, sessionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var cartDTO = objectMapper.readValue(jsonResponse, CartDTO.class);

        // THEN
        assertThat(cartDTO).isNotNull();
        assertThat(cartDTO.getSessionId()).isEqualTo(sessionId);
        assertThat(cartDTO.getCartTotal()).isEqualTo(new BigDecimal("110.00"));
        assertThat(cartDTO.getCartTotalWithDiscount()).isEqualTo(new BigDecimal("100.00"));

        assertThat(cartDTO.getCartItems()).isNotNull();

        assertThat(cartDTO.getCartItems().size()).isEqualTo(2);

        assertThat(cartDTO.getCartItems().get(0).getBarcode()).isEqualTo("3333333333333");
        assertThat(cartDTO.getCartItems().get(0).getProductName()).isEqualTo("Karma dla koni");
        assertThat(cartDTO.getCartItems().get(0).getQuantity()).isEqualTo(3);
        assertThat(cartDTO.getCartItems().get(0).getUnitPrice()).isEqualTo(new BigDecimal("30.00"));

        assertThat(cartDTO.getCartItems().get(1).getBarcode()).isEqualTo("2222222222222");
        assertThat(cartDTO.getCartItems().get(1).getProductName()).isEqualTo("Psia karma");
        assertThat(cartDTO.getCartItems().get(1).getQuantity()).isEqualTo(2);
        assertThat(cartDTO.getCartItems().get(1).getUnitPrice()).isEqualTo(new BigDecimal("10.00"));
    }

    @Test
    void shouldIncludeBundlePromotion() throws Exception {
        // GIVEN
        var sessionId = "bundle-cart-session-123";

        var mvcResult = mockMvc.perform(get("/cart")
                        .header(SESSION_HEADER, sessionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var cartDTO = objectMapper.readValue(jsonResponse, CartDTO.class);

        // THEN
        assertThat(cartDTO).isNotNull();
        assertThat(cartDTO.getSessionId()).isEqualTo(sessionId);
        assertThat(cartDTO.getCartTotal()).isEqualTo(new BigDecimal("70.00"));
        assertThat(cartDTO.getCartTotalWithDiscount()).isEqualTo(new BigDecimal("65.00"));

        assertThat(cartDTO.getCartItems()).isNotNull();

        assertThat(cartDTO.getCartItems().size()).isEqualTo(2);

        assertThat(cartDTO.getCartItems().get(0).getBarcode()).isEqualTo("4444444444444");
        assertThat(cartDTO.getCartItems().get(0).getProductName()).isEqualTo("Boomerang");
        assertThat(cartDTO.getCartItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(cartDTO.getCartItems().get(0).getUnitPrice()).isEqualTo(new BigDecimal("15.00"));

        assertThat(cartDTO.getCartItems().get(1).getBarcode()).isEqualTo("5555555555555");
        assertThat(cartDTO.getCartItems().get(1).getProductName()).isEqualTo("Maska Batmana");
        assertThat(cartDTO.getCartItems().get(1).getQuantity()).isEqualTo(1);
        assertThat(cartDTO.getCartItems().get(1).getUnitPrice()).isEqualTo(new BigDecimal("40.00"));
    }

    @Test
    void shouldCreateCart_AndAddItem() throws Exception {
        // GIVEN
        var sessionId = "not-existing-cart-session-123";

        var request = new CreateCartItemRequest("4444444444444", 1);

        var mvcResult = mockMvc.perform(post("/cart/item")
                        .header(SESSION_HEADER, sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // WHEN
        var jsonResponse = mvcResult.getResponse().getContentAsString();
        var cartDTO = objectMapper.readValue(jsonResponse, CartDTO.class);

        // THEN
        assertThat(cartDTO).isNotNull();
        assertThat(cartDTO.getSessionId()).isEqualTo(sessionId);
        assertThat(cartDTO.getCartTotal()).isEqualTo(new BigDecimal("15.00"));
        assertThat(cartDTO.getCartTotalWithDiscount()).isEqualTo(new BigDecimal("15.00"));

        assertThat(cartDTO.getCartItems()).isNotNull();
        assertThat(cartDTO.getCartItems()).hasSize(1);

        assertThat(cartRepository.findBySessionId(sessionId)).isPresent();
    }

}