package com.pragmaticcoders.petshop.mapper;

import com.pragmaticcoders.petshop.dto.CartDTO;
import com.pragmaticcoders.petshop.model.CartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = CartItemDTOMapper.class)
public interface CartDTOMapper {

    @Mapping(source = "sessionId", target = "sessionId")
    @Mapping(source = "cartItems", target = "cartItems")
    CartDTO toCartDTO(CartEntity cartEntity);
}
